package com.clicks.fulafiaresultcheckingverificationsystem.utils;

import com.clicks.fulafiaresultcheckingverificationsystem.dtos.*;
import com.clicks.fulafiaresultcheckingverificationsystem.enums.CourseType;
import com.clicks.fulafiaresultcheckingverificationsystem.model.course.CourseGrade;
import com.clicks.fulafiaresultcheckingverificationsystem.model.department.Department;
import com.clicks.fulafiaresultcheckingverificationsystem.model.department.DepartmentCourse;
import com.clicks.fulafiaresultcheckingverificationsystem.model.student.Student;
import com.clicks.fulafiaresultcheckingverificationsystem.model.student.StudentRegisteredCourse;
import com.clicks.fulafiaresultcheckingverificationsystem.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DtoMapper {

    private CourseService courseService;

    public  Function<DepartmentCourseDto, DepartmentCourse> departmentCourseDtoConverter = (course) ->
            DepartmentCourse.builder()
                    .level(course.level())
                    .type(CourseType.valueOf(course.type()))
                    .course(courseService.findCourseByCode(course.course()))
                    .build();

    public Function<DepartmentCourse, DepartmentCourseDto> departmentCourseToDepartmentCourseDto = (departmentCourse) ->
            new DepartmentCourseDto(
                    departmentCourse.getLevel(),
                    departmentCourse.getType().name(),
                    departmentCourse.getCourse().getTitle(),
                    departmentCourse.getCourse().getCode());

    public  Function<Department, DepartmentDto> departmentDtoConverter = (department) ->
            new DepartmentDto(
                    department.getName(),
                    department.getCode(),
                    department.getCourses()
                            .stream()
                            .map(departmentCourseToDepartmentCourseDto)
                            .toList()
            );

    public Function<StudentRegisteredCourse, CourseDto> studentCourseToCourseDto = (course) ->
            new CourseDto(
                    course.getCourse().getTitle(),
                    course.getCourse().getCode(),
                    course.getCourse().getSemester().name(),
                    course.getCourse().getUnit());

    public Function<Student, StudentDto> studentToStudentDto = (student) ->
            new StudentDto(
                    student.getName(),
                    student.getPhone(),
                    student.getMatric(),
                    student.getEmail(),
                    student.getDepartment().getName(),
                    false,
                    student.getCourses().stream()
                            .map(studentCourseToCourseDto)
                            .toList()
            );

    public Function<StudentRegisteredCourse, StudentRegisteredCourseDto> registeredCourseDtoConverter = (course) ->
            new StudentRegisteredCourseDto(
                    course.getCourse().getCode(),
                    course.getCourseStatus().name(),
                    course.getCurrentSemester().name(),
                    course.getCurrentLevel(),
                    course.getCurrentSession());

    public Function<CourseGrade, CourseGradeDto> courseGradeDtoMapper = (courseGrade) ->
            new CourseGradeDto(
                    courseGrade.getCourse().getCourse().getCode(),
                    courseGrade.getCourse().getCourse().getTitle(),
                    courseGrade.getCourse().getCourse().getUnit(),
                    courseGrade.getGrade().getGrade(),
                    courseGrade.getGrade().getGp(),
                    courseGrade.getGrade().getRemark()
            );

    @Autowired
    public DtoMapper(CourseService courseService) {
        this.courseService = courseService;
    }
}
