package com.clicks.fulafiaresultcheckingverificationsystem.service;

import com.clicks.fulafiaresultcheckingverificationsystem.dtos.NewStudentResponseDto;
import com.clicks.fulafiaresultcheckingverificationsystem.utils.DtoMapper;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.StudentDto;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.StudentRegisteredCourseDto;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.NewStudentRequest;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.RegisterStudentCoursesRequest;
import com.clicks.fulafiaresultcheckingverificationsystem.enums.CourseStatus;
import com.clicks.fulafiaresultcheckingverificationsystem.enums.Level;
import com.clicks.fulafiaresultcheckingverificationsystem.exceptions.InvalidRequestParamException;
import com.clicks.fulafiaresultcheckingverificationsystem.exceptions.ResourceNotFoundException;
import com.clicks.fulafiaresultcheckingverificationsystem.model.ResultGeneralCredential;
import com.clicks.fulafiaresultcheckingverificationsystem.model.student.Student;
import com.clicks.fulafiaresultcheckingverificationsystem.model.student.StudentRegisteredCourse;
import com.clicks.fulafiaresultcheckingverificationsystem.model.student.StudentResult;
import com.clicks.fulafiaresultcheckingverificationsystem.repository.StudentRegisteredCourseRepository;
import com.clicks.fulafiaresultcheckingverificationsystem.repository.StudentRepository;
import com.clicks.fulafiaresultcheckingverificationsystem.repository.StudentResultRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import static com.clicks.fulafiaresultcheckingverificationsystem.enums.CourseStatus.FAILED;
import static com.clicks.fulafiaresultcheckingverificationsystem.enums.CourseStatus.PENDING;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentRegisteredCourseRepository studentRegisteredCourseRepository;
    private final StudentResultRepository studentResultRepository;
    private final CourseService courseService;
    private final ResultGeneralCredentialService resultGeneralCredentialService;
    private final DtoMapper dtoMapper;
    private final DepartmentService departmentService;

    /**
     * Function registers new student to the system
     *
     * @param newStudentRequest contains detailed information of this new students
     * @return {@link NewStudentResponseDto} containing information of the newly registered student
     */
    public NewStudentResponseDto addNewStudent(NewStudentRequest newStudentRequest) {

        //Generate new result passcode for this user
        //THis is used for USSD result checking
        int passcode = new Random().nextInt(100000, 999999);

        ResultGeneralCredential resultGeneralCredential =
                resultGeneralCredentialService.getResultGeneralCredential();

        int currentLevel = Level.valueOf(newStudentRequest.level().replace(" ", "_")).getValue();

        String studentName = newStudentRequest.name();
        String providedPhone = newStudentRequest.phone();
        String studentPhone = providedPhone.startsWith("0") ? "+234" + providedPhone.substring(1) : providedPhone;

        String resultCode = buildResultCode(studentName, studentPhone);

        //Get all registered courses for this new student
        List<StudentRegisteredCourse> savedStudentCourses = getStudentRegisteredCourses(
                newStudentRequest, resultGeneralCredential, currentLevel);

        //Build Student Result for this student
        StudentResult savedstudentResult = getStudentResult(resultGeneralCredential, currentLevel, resultCode);

        //Build new student object
        Student newStudent = Student.builder()
                .phone(studentPhone)
                .passcode(String.valueOf(passcode))
                .matric(newStudentRequest.matric())
                .department(departmentService.findDepartmentByName(newStudentRequest.department()))
                .name(studentName)
                .email(newStudentRequest.email())
                .courses(savedStudentCourses)
                .currentLevel(currentLevel)
                .studentResult(savedstudentResult)
                .build();

        //Save to database
        Student savedStudent = studentRepository.saveAndFlush(newStudent);

        return new NewStudentResponseDto(studentName, savedStudent.getMatric(), savedStudent.getStudentResult().getResultCode(), savedStudent.getPasscode());
    }

    private String buildResultCode(String name, String phone) {

        int nameLength = name.length();

        return phone.replace("+234", "")
                .concat(String.valueOf(nameLength < 10 ? "0" + nameLength : nameLength));
    }


    public List<StudentDto> getStudents(Integer page, Optional<String> all) {

        PageRequest pg = PageRequest.of((page < 1) ? 0 : (page - 1), 10);

        if(all.isPresent()) {
            return  studentRepository.findAll(pg)
                    .map(dtoMapper.studentToStudentDto)
                    .toList();

        }else {
            return studentRepository.findAll(pg)
                    .map(this::buildStudentDto)
                    .toList();
        }


    }

    /**
     * Function to find DTO of a Student
     *
     * @param matric student unique matric number
     * @return an object of {@link StudentDto}
     */
    public StudentDto findStudentDtoByMatric(String matric) {

        //Find this student using matriculation number
        return buildStudentDto(findStudentByMatric(matric));
    }

    /**
     * Function to register new courses for a student in a particular level
     *
     * @param matric student unique matriculation number
     * @param registerStudentCoursesRequest a DTO that contains all data for course registration
     */

    public void registerCourses(String matric, RegisterStudentCoursesRequest registerStudentCoursesRequest) {

        ResultGeneralCredential resultGeneralCredential = resultGeneralCredentialService.getResultGeneralCredential();
       try {
           //Find student with this matric number
           Student student = findStudentByMatric(matric);

           //Get all registered courses for this student
           List<String> studentCourses = student.getCourses()
                   .stream()
                   .map(co -> co.getCourse().getCode())
                   .toList();

           //Create new level for this student
           int newStudentLevel = student.getCurrentLevel() + 1;

           //Get all course models from the course codes selected by the student
           List<StudentRegisteredCourse> newCourses = registerStudentCoursesRequest.courses()
                   .stream()
                   .filter(code -> !studentCourses.contains(code))
                   .map(courseService::findCourseByCode)
                   .map(course -> StudentRegisteredCourse.builder()
                           .currentSemester(resultGeneralCredential.getCurrentSemester())
                           .currentLevel(newStudentLevel)
                           .courseStatus(PENDING)
                           .course(course)
                           .currentSession(resultGeneralCredential.getCurrentSession())
                           .build())
                   .toList();

           List<StudentRegisteredCourse> studentRegisteredCourses =
                   studentRegisteredCourseRepository.saveAllAndFlush(newCourses);

           student.getCourses().addAll(studentRegisteredCourses);

           //Set student current level
           student.setCurrentLevel(newStudentLevel);

       } catch (Exception e) {
           throw new InvalidRequestParamException("Invalid level ");
       }
    }

    /**
     * Function to find a student using matric number
     *
     * @param matric student matriculation number
     * @return a {@link Student}
     */
    public Student findStudentByMatric(String matric) {
        return studentRepository.findStudentByMatric(matric)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid matric number " + matric));
    }

    public Student findStudentByPhone(String phone) {
        return studentRepository.findStudentByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("No record for student with phone number " + phone));
    }

    public List<StudentRegisteredCourseDto> getStudentCourses(String matric, String status) {

        //Get this student by provided matric number
        Student student = findStudentByMatric(matric);

        //Check if a status is provided
        if(Objects.nonNull(status)) {

            //Gets FAILED and PENDING courses for this student
            //Used for course registration
            if (Objects.equals(status, "INIT")) {

                return student.getCourses().stream()
                        .filter(co -> co.getCourseStatus().equals(PENDING) || co.getCourseStatus().equals(FAILED))
                        .map(dtoMapper.registeredCourseDtoConverter)
                        .toList();
            }

            try {
                CourseStatus courseStatus = CourseStatus.valueOf(status);

                return student.getCourses().stream()
                        .filter(co -> co.getCourseStatus().equals(courseStatus))
                        .map(dtoMapper.registeredCourseDtoConverter)
                        .toList();

            }catch (IllegalArgumentException exception) {
                throw new InvalidRequestParamException("Invalid status value " + status);
            }
        }

        return student.getCourses().stream()
                .map(dtoMapper.registeredCourseDtoConverter)
                .toList();
    }


    private StudentDto buildStudentDto(Student student) {
        return new StudentDto(
                student.getName(),
                student.getPhone(),
                student.getMatric(),
                student.getEmail(),
                student.getDepartment().getName(),
                false,
                student.getPasscode(),
                student.getStudentResult().getResultCode(),
                student.getCourses().stream()
                        .filter(course -> course.getCourseStatus().equals(PENDING) || course.getCourseStatus().equals(FAILED) && !(
                                        course.getCurrentSession().equals(resultGeneralCredentialService.getResultGeneralCredential().getCurrentSession()) &&
                                        course.getCurrentSemester().equals(resultGeneralCredentialService.getResultGeneralCredential().getCurrentSemester())))
                        .map(dtoMapper.studentCourseToCourseDto)
                        .toList());
    }

    private StudentResult getStudentResult(ResultGeneralCredential resultGeneralCredential, int currentLevel, String resultCode) {

        StudentResult studentResult = StudentResult.builder()
                .currentLevel(currentLevel)
                .resultCode(resultCode)
                .currentRemarks("")
                .currentSession(resultGeneralCredential.getCurrentSession())
                .currentSemester(resultGeneralCredential.getCurrentSemester())
                .build();

        return studentResultRepository.saveAndFlush(studentResult);
    }

    private List<StudentRegisteredCourse> getStudentRegisteredCourses(NewStudentRequest newStudentRequest, ResultGeneralCredential resultGeneralCredential, int currentLevel) {

        List<StudentRegisteredCourse> studentCourses = newStudentRequest.courses()
                .stream()
                .map(courseService::findCourseByCode)
                .map(course -> StudentRegisteredCourse.builder()
                        .courseStatus(PENDING)
                        .currentSession(resultGeneralCredential.getCurrentSession())
                        .currentLevel(currentLevel)
                        .course(course)
                        .currentSemester(resultGeneralCredential.getCurrentSemester())
                        .build())
                .toList();

        return studentRegisteredCourseRepository.saveAllAndFlush(studentCourses);
    }

}
