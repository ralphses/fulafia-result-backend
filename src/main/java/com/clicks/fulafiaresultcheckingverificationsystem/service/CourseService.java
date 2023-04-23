package com.clicks.fulafiaresultcheckingverificationsystem.service;

import com.clicks.fulafiaresultcheckingverificationsystem.dtos.CourseDto;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.NewCourseRequest;
import com.clicks.fulafiaresultcheckingverificationsystem.enums.Semester;
import com.clicks.fulafiaresultcheckingverificationsystem.exceptions.InvalidRequestParamException;
import com.clicks.fulafiaresultcheckingverificationsystem.exceptions.ResourceNotFoundException;
import com.clicks.fulafiaresultcheckingverificationsystem.model.course.Course;
import com.clicks.fulafiaresultcheckingverificationsystem.model.department.Department;
import com.clicks.fulafiaresultcheckingverificationsystem.repository.CourseRepository;
import com.clicks.fulafiaresultcheckingverificationsystem.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
//    private final DepartmentService departmentService;
    private final DepartmentRepository departmentRepository;

    /**
     * Function to add new Course to the system
     *
     * @param newCourseRequest DTO containing information of the new course
     */
    public void addNewCourse(NewCourseRequest newCourseRequest) {

       try {
           Semester semester = Semester.valueOf(newCourseRequest.semester().toUpperCase());

           Course newCourse = Course.builder()
                   .code(newCourseRequest.code())
                   .title(newCourseRequest.title())
                   .unit(newCourseRequest.unit())
                   .semester(semester)
                   .build();

           courseRepository.save(newCourse);

       } catch (Exception exception) {
           throw new InvalidRequestParamException("invalid semester or level");
       }
    }

    public List<CourseDto> getCourses(Integer page, Optional<String> departmentName) {

        return departmentName.map(s -> departmentRepository.findDepartmentByName(s)
                        .orElseThrow(() -> new ResourceNotFoundException("Invalid department name " + s))
                        .getCourses()
                        .stream()
                        .map(course -> new CourseDto(
                                course.getCourse().getTitle(),
                                course.getCourse().getCode(),
                                course.getCourse().getSemester().name(),
                                course.getCourse().getUnit()))
                        .toList())
                .orElseGet(() -> courseRepository.findAll(PageRequest.of((page < 1) ? 0 : (page - 1), 10))
                        .map(course -> new CourseDto(
                                course.getTitle(),
                                course.getCode(),
                                course.getSemester().name(),
                                course.getUnit()))
                        .toList());
    }

    public void deleteCourse(String code) {
        courseRepository.findCourseByCode(code).ifPresent(courseRepository::delete);
    }

    /**
     * Function to update an existing {@link Course}
     *
     * @param code course code of the course to be updates
     * @param newCourseRequest DTO containing all new data to be updated
     */
    public void updateCourse(String code, NewCourseRequest newCourseRequest) {

        //Get this course
        Course course = courseRepository.findCourseByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid course code " + code));

        //Update course details
        course.setCode(newCourseRequest.code());
        course.setUnit(newCourseRequest.unit());
        course.setTitle(newCourseRequest.title());
    }

    /**
     * Function to find a course by its ID
     *
     * @param courseId the course ID
     * @return a {@link Course}
     */
    public Course findCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    public Course findCourseByTitle(String title) {
        return courseRepository.findCourseByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Course title " + title));
    }

    public Course findCourseByCode(String code) {
        return courseRepository.findCourseByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Course Code " + code));
    }
}
