package com.clicks.fulafiaresultcheckingverificationsystem.controller;

import com.clicks.fulafiaresultcheckingverificationsystem.dtos.CourseDto;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.NewCourseRequest;
import com.clicks.fulafiaresultcheckingverificationsystem.service.CourseService;
import com.clicks.fulafiaresultcheckingverificationsystem.utils.ResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping(value = "/add")
    public ResponseEntity<ResponseMessage> add(@RequestBody @Valid NewCourseRequest newCourseRequest) {

        courseService.addNewCourse(newCourseRequest);

        return ResponseEntity.ok(new ResponseMessage("SUCCESS", 0, Map.of()));
    }

    @GetMapping(value = "/all/{page}")
    public ResponseEntity<ResponseMessage> getCourses(
            @PathVariable(required = false) Integer page,
            @RequestParam(required = false, name = "department") String department,
            @RequestParam(required = false, name = "matric") String matric)  {

        List<CourseDto> courses = courseService.getCourses(
                Optional.of(page).orElse(1),
                Optional.ofNullable(department),
                Optional.ofNullable(matric));

        return ResponseEntity.ok(new ResponseMessage("SUCCESS", 0, Map.of("courses", courses)));

    }

    @DeleteMapping(value = "/delete/{code}")
    public ResponseEntity<ResponseMessage> deleteCourse(@PathVariable String code) {
        courseService.deleteCourse(code);
        return ResponseEntity.ok(new ResponseMessage("SUCCESS", 0, Map.of()));

    }

    @PutMapping(value = "/update/{code}")
    public ResponseEntity<ResponseMessage> updateCourse(
            @PathVariable String code, @RequestBody NewCourseRequest newCourseRequest) {

        courseService.updateCourse(code, newCourseRequest);

        return ResponseEntity.ok(new ResponseMessage("SUCCESS", 0, Map.of()));

    }

}
