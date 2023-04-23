package com.clicks.fulafiaresultcheckingverificationsystem.controller;

import com.clicks.fulafiaresultcheckingverificationsystem.dtos.DepartmentCourseDto;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.DepartmentDto;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.AddDepartmentCoursesRequest;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.NewDepartmentRequest;
import com.clicks.fulafiaresultcheckingverificationsystem.service.DepartmentService;
import com.clicks.fulafiaresultcheckingverificationsystem.utils.ResponseMessage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
//@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("/api/v1/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping(value = "/{page}")
    public ResponseEntity<List<DepartmentDto>> getDepartments(@PathVariable Integer page) {
        List<DepartmentDto> departments = departmentService.getDepartments(page);
        return ResponseEntity.ok( departments);
    }

    @PostMapping(value = "/new")
    public ResponseEntity<ResponseMessage> addDepartment(
            @Valid @RequestBody NewDepartmentRequest newDepartmentRequest){

        departmentService.addDepartment(newDepartmentRequest);

        return new ResponseEntity<>(new ResponseMessage("SUCCESS", 0, Map.of()), OK);
    }

    @PostMapping(value = "/add-courses")
    public ResponseEntity<ResponseMessage> addNewCourses(@Valid @RequestBody AddDepartmentCoursesRequest addDepartmentCoursesRequest) {

        departmentService.addNewCourses(addDepartmentCoursesRequest);

        return new ResponseEntity<>(new ResponseMessage("SUCCESS", 0, Map.of()), OK);
    }

    @GetMapping(value = "/{code}/courses")
    public ResponseEntity<ResponseMessage> courses(@PathVariable @NotBlank String code) {
        List<DepartmentCourseDto> courses = departmentService.getCourses(code);
        return new ResponseEntity<>(new ResponseMessage("SUCCESS", 0, Map.of("courses", courses)), OK);
    }

    @GetMapping(value = "")
    public ResponseEntity<ResponseMessage> getDepartment(@RequestParam(value = "department") String department) {
        return ResponseEntity.ok(new ResponseMessage("SUCCESS", 0, Map.of("department", departmentService.findDepartment(department))));
    }
}
