package com.clicks.fulafiaresultcheckingverificationsystem.service;

import com.clicks.fulafiaresultcheckingverificationsystem.dtos.ResultAnalysisDto;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.UssdRequest;
import com.clicks.fulafiaresultcheckingverificationsystem.exceptions.InvalidRequestParamException;
import com.clicks.fulafiaresultcheckingverificationsystem.exceptions.ResourceNotFoundException;
import com.clicks.fulafiaresultcheckingverificationsystem.model.course.Course;
import com.clicks.fulafiaresultcheckingverificationsystem.model.student.*;
import com.clicks.fulafiaresultcheckingverificationsystem.repository.ResultAnalysisRepository;
import com.clicks.fulafiaresultcheckingverificationsystem.repository.StudentResultRepository;
import com.clicks.fulafiaresultcheckingverificationsystem.utils.DtoMapper;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.CourseGradeDto;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.StudentResultDto;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.NewStudentResultDto;
import com.clicks.fulafiaresultcheckingverificationsystem.enums.Grade;
import com.clicks.fulafiaresultcheckingverificationsystem.enums.Semester;
import com.clicks.fulafiaresultcheckingverificationsystem.model.*;
import com.clicks.fulafiaresultcheckingverificationsystem.model.course.CourseGrade;
import com.clicks.fulafiaresultcheckingverificationsystem.repository.CourseGradeRepository;
import com.clicks.fulafiaresultcheckingverificationsystem.repository.StudentResultCourseRepository;
import com.clicks.fulafiaresultcheckingverificationsystem.utils.SessionCompare;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

import static com.clicks.fulafiaresultcheckingverificationsystem.enums.CourseStatus.*;
import static java.util.Arrays.stream;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StudentResultService {

    private final DtoMapper dtoMapper;
    private final CourseService courseService;
    private final StudentService studentService;
    private final CourseGradeRepository courseGradeRepository;
    private final StudentResultRepository studentResultRepository;
    private final ResultAnalysisRepository resultAnalysisRepository;
    public static final Map<String, Object> responseList = new HashMap<>();
    private final StudentResultCourseRepository studentResultCourseRepository;
    private final ResultGeneralCredentialService resultGeneralCredentialService;


    public String handleUssdRequest(UssdRequest ussdRequest) {

        String text = ussdRequest.text();

        //User makes first request to the welcome page
        if(text.equals("")) {

            String response = responseBuilder(
                    "Welcome to FULafia Results",
                    "Select one option below",
                    "1. Student Check Result",
                    "2. Authenticate Result");

            return prepareResponse(response, false);
        }

        //When user selects 1 to check result
        else if(text.equals("1")) {

            return prepareResponse(responseBuilder("Enter Result Passcode"), false);
        }

        //When student enters result checking passcode
        else if(text.startsWith("1*") && text.replace("1*", "").matches("^[0-9]{6}$")) {

            Student student = studentService.findStudentByPhone(ussdRequest.phoneNumber());
            String passcode = text.replace("1*", "");

            if(passcode.equals(student.getPasscode())) {

                String response = responseBuilder(
                        "Select Semester",
                        "1. First semester",
                        "2. Second semester");

                return prepareResponse(response, false);
            }

            return prepareResponse(responseBuilder("Incorrect passcode"), true);
        }

        //When user is to select a semester (first)
        else if (text.replace("*", "").matches("^1[0-9]{6}1$")) {

            responseList.put("semester", "FIRST");

            String response = responseBuilder(
                    "Enter Session in the format YYYY0YYYY",
                    "e.g 202102022 for 2021/2022");

            return prepareResponse(response, false);
        }

        //When user is to select a semester (second)
        else if (text.replace("*", "").matches("^1[0-9]{6}2$")) {

            responseList.put("semester", "SECOND");

            String response = responseBuilder(
                    "Enter Session in the format YYYY0YYYY",
                    "e.g 202102022 for 2021/2022");

            return prepareResponse(response, false);
        }

        //When student enters session
        else if (text.replace("*", "").matches("^1[0-9]{6}[1-2][0-9]{4}0[0-9]{4}$")) {

            String sessionInput = text.substring(11);

            String session = sessionInput.substring(0, 4)
                    .concat("/")
                    .concat(sessionInput.substring(5));

            String phoneNumber = ussdRequest.phoneNumber();

            try {

                String semester = (String) responseList.get("semester");

                if(Objects.isNull(semester)) {
                    throw new ResourceNotFoundException("Invalid Input");
                }

                Student student = studentService.findStudentByPhone(phoneNumber);

                StudentResultDto studentResult = getStudentResult(student.getMatric(), Optional.of(semester), Optional.of(session));

                String response = studentResult.courseGrades().isEmpty() ?
                        responseBuilder(
                                "Session: ".concat(studentResult.session()),
                                "Semester: ".concat(studentResult.semester()),
                                "No Result found"
                        ) :
                        responseBuilder(
                                "Session: ".concat(studentResult.session()),
                                "Semester: ".concat(studentResult.semester()),
                                "Name: ".concat(studentResult.name()),
                                "Level ".concat(studentResult.level()),
                                prepareResult(studentResult.courseGrades()),
                                "TCE: ".concat(studentResult.current().totalCreditEarned()),
                                "TCL: ".concat(studentResult.current().totalCreditLoad()),
                                "CGPA: ".concat(studentResult.current().gradePointAverage()),
                                "Remarks: ".concat(studentResult.remarks())
                );


                return prepareResponse(response, true);

            }catch (ResourceNotFoundException | NullPointerException exception) {

                return prepareResponse(responseBuilder("Invalid input"), true);
            }
        }
        else if (text.equals("2")) {

            return prepareResponse(responseBuilder("Enter Result Verification Code on certificate"), false);
        }
        else if (text.replace("*", "").matches("^2[0-9]{12}$")) {

            String resultCode = text.substring(2);

            try {
                StudentResult result = getStudentResultByResultCode(resultCode);

                if(Objects.isNull(result.getCurrentResultAnalysis())) {
                    throw new ResourceNotFoundException("AUTHENTIC\n But no Result found uploaded yet");
                }
                String classDegree = getClassDegree(result.getCurrentResultAnalysis().getGradePointAverage());

                String institutionName = "FEDERAL UNIVERSITY OF LAFIA";

                String studentName = studentService.findStudentByPhone(ussdRequest.phoneNumber()).getName();

                String graduationYear = result.getCurrentSession().substring(5);

                String response = responseBuilder(
                        "AUTHENTIC",
                        "Student name: ".concat(studentName.toUpperCase()),
                        "Institution: ".concat(institutionName),
                        "Class of degree: ".concat(classDegree),
                        "Graduation year: ".concat(graduationYear)
                );

                return prepareResponse(response, true);

            } catch (ResourceNotFoundException exception) {

                return prepareResponse(responseBuilder(exception.getMessage()), true);
            }
        }
        else {
            return prepareResponse("Invalid selection", true);
        }
    }


    public void addNewStudentResult(NewStudentResultDto newStudentResultDto) {

        ResultGeneralCredential resultGeneralCredential =
                resultGeneralCredentialService.getResultGeneralCredential();

        Map<String, String> courseScoreMap = new HashMap<>();
        newStudentResultDto.courseScore().forEach(course -> courseScoreMap.put(course.course(), course.score()));

        //Get Student with matric number
        Student student = studentService.findStudentByMatric(newStudentResultDto.matric());

        //Get student registered courses
        List<String> courses = student.getCourses()
                .stream()
                .map(c -> c.getCourse().getCode())
                .toList();

        //Check if result is already uploaded
        List<String> resultCourses = student.getStudentResult().getCourseGrades()
                .stream()
                .filter(courseGrade ->
                        Objects.equals(courseGrade.getCourse().getCurrentSession(), resultGeneralCredential.getCurrentSession()) &&
                        Objects.equals(courseGrade.getCourse().getCurrentSemester(), resultGeneralCredential.getCurrentSemester()))
                .map(CourseGrade::getCourse)
                .map(c -> c.getCourse().getCode())
                .toList();

        //Prepare student courses and their grades
        List<CourseGrade> currentCourseGrades = courseScoreMap.entrySet().stream()
                .filter(co -> !resultCourses.contains(co.getKey()))
                .map(result -> {

                    StudentResultCourse savedStudentResultCourse = getStudentResultCourse(resultGeneralCredential, student, result);

                    Grade studentGradeForCourse = getGrade(result);

                    return CourseGrade.builder()
                            .course(savedStudentResultCourse)
                            .grade(studentGradeForCourse)
                            .level(student.getCurrentLevel())
                            .build();
                }).toList();


        //Get all failed courses
        List<String> failedCourses = getFailedOrPendingCourses(currentCourseGrades, Grade.FAIL);

        //Get all pending courses
        List<String> pendingCourses = courses.stream()
                .filter(co -> !courseScoreMap.containsKey(co))
                .toList();

        //Update student registered courses to reflect failed courses
        student.getCourses().forEach(course -> {

            String courseCode = course.getCourse().getCode();

            if (failedCourses.contains(courseCode)) {
                course.setCourseStatus(FAILED);
            } else if (!pendingCourses.contains(courseCode)) {
                course.setCourseStatus(PASSED);
            }
        });

        //Prepare remarks for both failed and pending courses
        String failedCoursesRemarks = (failedCourses.isEmpty()) ? "" : "REPEAT ".concat(String.join(", ", failedCourses));
        String pendingCoursesRemarks = (pendingCourses.isEmpty()) ? "" : "TAKE ".concat(String.join(", ", pendingCourses));

        String remark = (failedCourses.isEmpty() && pendingCourses.isEmpty()) ?
                "PASSED" :
                failedCoursesRemarks
                        .concat("\n")
                        .concat(pendingCoursesRemarks);

        StudentResult studentResult = student.getStudentResult();

        List<CourseGrade> savedCourseGrades = courseGradeRepository.saveAllAndFlush(currentCourseGrades)
                .stream()
                .filter(courseGrade -> !resultCourses.contains(courseGrade.getCourse().getCourse().getCode()))
                .toList();

        if(savedCourseGrades.isEmpty()) {
            throw new InvalidRequestParamException("Result already uploaded for courses " + resultCourses);
        }

        //Create result detail
        ResultAnalysis resultAnalysis = resultAnalysisRepository.saveAndFlush(getResultAnalysis(savedCourseGrades));

        studentResult.setCurrentRemarks(remark);
        studentResult.getCourseGrades().addAll(savedCourseGrades);
        studentResult.setCurrentLevel(studentResult.getCurrentLevel());
        studentResult.setCurrentSession(resultGeneralCredential.getCurrentSession());
        studentResult.setCurrentSession(resultGeneralCredential.getCurrentSession());
        studentResult.setCurrentResultAnalysis(resultAnalysis);

    }

    public StudentResultDto getStudentResult(String matric, Optional<String> semester, Optional<String> session) {

        Student student = studentService.findStudentByMatric(matric);

        return prepareStudentResult(student, semester, session);
    }


    public StudentResult getStudentResultByResultCode(String resultCode) {
        return studentResultRepository.findStudentResultByResultCode(resultCode)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid  Verification Code " + resultCode));
    }

    private String prepareResponse(String message, boolean lastResponse) {
        return (lastResponse) ?
                "END ".concat(message) :
                "CON ".concat(message);
    }

    private List<CourseGrade> getCourseGrades(Optional<String> semester, Optional<String> session, StudentResult studentResult) {

        if (semester.isPresent() && session.isPresent()) {
            return getCourseGradeList(semester, session, studentResult);
        }
        else if (session.isPresent()) {
            return getCourseGrades(session, studentResult);
        }
        else return studentResult.getCourseGrades();
    }

    private static List<CourseGrade> getCourseGradeList(Optional<String> semester, Optional<String> session, StudentResult studentResult) {

        return studentResult.getCourseGrades()
                .stream()
                .filter(courseGrade -> (
                                courseGrade.getCourse().getCurrentSemester().equals(Semester.valueOf(semester.get())) &&
                                SessionCompare.compareSession(courseGrade.getCourse().getCurrentSession(), session.get())))
                .toList();
    }

    private List<CourseGrade> getCourseGrades(Optional<String> session, StudentResult studentResult){

        return studentResult.getCourseGrades()
                .stream()
                .filter(courseGrade -> SessionCompare.compareSession(courseGrade.getCourse().getCurrentSession(), session.get()))
                .toList();
    }

    private List<CourseGradeDto> getCourseGradeDtos(List<CourseGrade> courseGrades) {
        return courseGrades.stream()
                .map(dtoMapper.courseGradeDtoMapper)
                .toList();
    }

    private List<String> getFailedOrPendingCourses(List<CourseGrade> currentCourseGrades, Grade grade) {
        return currentCourseGrades.stream()
                .filter(courseGrade -> courseGrade.getGrade().equals(grade))
                .map(courseGrade -> courseGrade.getCourse().getCourse().getCode())
                .toList();
    }

    private int calculateTotalCreditEarned(List<CourseGrade> courseGrades) {

        return courseGrades.stream()
                .filter(courseGrade -> !courseGrade.getGrade().equals(Grade.FAIL))
                .map(getCourseGradePoint())
                .reduce(Integer::sum)
                .orElse(0);
    }

    private ResultAnalysis getResultAnalysis(List<CourseGrade> latestCourseGrades) {

        //Calculate total credit load
        int totalCreditLoad = calculateTotalCreditLoad(latestCourseGrades);

        //Calculate total grade point
        int totalGradePoint = calculateTotalGradePoint(latestCourseGrades);

        //Calculate total Credit earned
        int totalCreditEarned = calculateTotalCreditEarned(latestCourseGrades);

        //Calculate Grade average point
        double gradeAveragePoint = (double) totalGradePoint / totalCreditLoad;

        return ResultAnalysis.builder()
                .totalCreditEarned((double) totalCreditEarned)
                .totalCreditLoad((double) totalCreditLoad)
                .totalGradePoint((double) totalGradePoint)
                .gradePointAverage(gradeAveragePoint)
                .build();
    }

    private int calculateTotalGradePoint(List<CourseGrade> courseGrades) {

        return courseGrades.stream()
                .map(this::getGradePointValue)
                .reduce(Integer::sum)
                .orElse(0);
    }

    private int getGradePointValue(CourseGrade courseGrade) {
        return Math.multiplyExact(courseGrade.getCourse().getCourse().getUnit(), courseGrade.getGrade().getGp());
    }

    private int calculateTotalCreditLoad(List<CourseGrade> courseGrades) {
        return courseGrades.stream()
                .map(getCourseGradePoint())
                .reduce(Integer::sum)
                .orElse(0);
    }

    private String prepareResult(List<CourseGradeDto> courseGrades) {

        StringBuilder resultBuilder = new StringBuilder();

        resultBuilder.append("S/N\t")
                .append("Course\t")
                .append("Grade\t")
                .append("\n");

        for (int i = 0; i < courseGrades.size(); i++) {
            resultBuilder
                    .append((i+1))
                    .append(". \t")
                    .append(courseGrades.get(i).courseCode())
                    .append("\t")
                    .append(courseGrades.get(i).grade())
                    .append("\n");
        }
        return resultBuilder.toString();
    }

    private Function<CourseGrade, Integer> getCourseGradePoint() {
        return courseGrade -> courseGrade.getCourse().getCourse().getUnit();
    }
    private Grade getGrade(Map.Entry<String, String> result) {

        return stream(Grade.values())
                .filter(grade -> grade.getGrade().equals(result.getValue()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid grade for " + result.getKey()));
    }

    private StudentResultDto prepareStudentResult(Student student, Optional<String> semester, Optional<String> session) {

        StudentResult studentResult = student.getStudentResult();

        Integer currentLevel = studentResult.getCurrentLevel();

        List<CourseGrade> latestCourseGrades = getCourseGrades(semester, session, studentResult);

        ResultAnalysis resultAnalysis = getResultAnalysis(latestCourseGrades);

        //Prepare course grade DTO
        List<CourseGradeDto> courseGradeDtos = getCourseGradeDtos(latestCourseGrades);

        ResultAnalysisDto resultAnalysisDto = new ResultAnalysisDto(
                String.format("%.2f", resultAnalysis.getTotalCreditLoad()),
                String.format("%.2f", resultAnalysis.getTotalCreditEarned()),
                String.format("%.2f", resultAnalysis.getTotalGradePoint()),
                String.format("%.2f", resultAnalysis.getGradePointAverage())
        );

        return new StudentResultDto(
                student.getName(),
                String.valueOf(currentLevel).charAt(0) + "00",
                session.orElse(studentResult.getCurrentSession()),
                semester.orElse(studentResult.getCurrentSemester().name()),
                studentResult.getCurrentRemarks(),
                resultAnalysisDto,
                courseGradeDtos);
    }

    private StudentResultCourse getStudentResultCourse(ResultGeneralCredential resultGeneralCredential, Student student, Map.Entry<String, String> result) {

        Course course = courseService.findCourseByCode(result.getKey());

        Optional<StudentResultCourse> old = studentResultCourseRepository
                .findByCurrentSemesterAndCurrentSessionAndCourse(
                        resultGeneralCredential.getCurrentSemester(),
                        resultGeneralCredential.getCurrentSession(),
                        course);

        if (old.isPresent()) return old.get();
        else {
            StudentResultCourse studentResultCourse = StudentResultCourse.builder()
                    .course(course)
                    .currentLevel(student.getCurrentLevel())
                    .currentSession(resultGeneralCredential.getCurrentSession())
                    .currentSemester(resultGeneralCredential.getCurrentSemester())
                    .build();
            return studentResultCourseRepository.saveAndFlush(studentResultCourse);
        }
    }

    private String responseBuilder(String... inputs) {
        return String.join("\n", inputs);
    }

    private String getClassDegree(Double gradePointAverage) {

        return (gradePointAverage >= 4.5) ? "FIRST CLASS" :
                (gradePointAverage < 4.5 && gradePointAverage >= 3.5) ? "SECOND CLASS UPPER" :
                        (gradePointAverage < 3.5 && gradePointAverage >= 2.4) ? "SECOND CLASS LOWER" :
                                (gradePointAverage < 2.4 && gradePointAverage >= 1.5) ? "THIRD CLASS" :
                                        (gradePointAverage < 1.5 && gradePointAverage >= 1.0) ? "PASS" :
                                                "PROBATION";
    }

}
