package com.clicks.fulafiaresultcheckingverificationsystem.controller;

import com.clicks.fulafiaresultcheckingverificationsystem.dtos.NewStudentResponseDto;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.StudentDto;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.StudentRegisteredCourseDto;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.NewStudentRequest;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.RegisterStudentCoursesRequest;
import com.clicks.fulafiaresultcheckingverificationsystem.service.StudentService;
import com.clicks.fulafiaresultcheckingverificationsystem.utils.ResponseMessage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/student")
public class StudentController {

    private final StudentService studentService;
    @PostMapping("/add")
    public ResponseEntity<ResponseMessage> addNewStudent(
            @RequestBody @Valid NewStudentRequest newStudentRequest) {

        log.info("Request {}", newStudentRequest);
        NewStudentResponseDto savedStudent = studentService.addNewStudent(newStudentRequest);

        return ResponseEntity.ok(new ResponseMessage("SUCCESS", 0, Map.of("student", savedStudent)));
    }

    @GetMapping(value = "/{page}")
    public ResponseEntity<ResponseMessage> getStudents(@PathVariable(required = false) Integer page, @RequestParam(name = "all", required = false) String all) {

        List<StudentDto> students = studentService
                .getStudents(Optional.of(page).orElse(1), Optional.ofNullable(all));

        return ResponseEntity.ok(new ResponseMessage("SUCCESS", 0, Map.of("students", students)));

    }

    @GetMapping(value = "/find/{matric}")
    public ResponseEntity<ResponseMessage> findStudentByMatric(@PathVariable @NotBlank String matric) {

        StudentDto student = studentService.findStudentDtoByMatric(matric);
        return ResponseEntity.ok(new ResponseMessage("SUCCESS", 0, Map.of("student", student)));
    }

    @PostMapping(value = "/register/course")
    public ResponseEntity<ResponseMessage> registerCourses(
            @RequestBody @Valid RegisterStudentCoursesRequest registerStudentCoursesRequest) {

        studentService.registerCourses(registerStudentCoursesRequest);

        return ResponseEntity.ok(new ResponseMessage("SUCCESS", 0, Map.of()));

    }

    @GetMapping(value = "/{matric}/courses")
    public ResponseEntity<ResponseMessage> getCourses(
            @PathVariable @NotBlank String matric,
            @RequestParam(value = "sort", required = false) String status) {

        List<StudentRegisteredCourseDto> registeredCourses = studentService.getStudentCourses(matric, status);

        return ResponseEntity.ok(new ResponseMessage("SUCCESS", 0, Map.of("courses", registeredCourses)));

    }
}
