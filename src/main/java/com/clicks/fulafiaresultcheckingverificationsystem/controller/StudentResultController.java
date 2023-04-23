package com.clicks.fulafiaresultcheckingverificationsystem.controller;

import com.clicks.fulafiaresultcheckingverificationsystem.dtos.StudentResultDto;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.NewStudentResultDto;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.UssdRequest;
import com.clicks.fulafiaresultcheckingverificationsystem.service.StudentResultService;
import com.clicks.fulafiaresultcheckingverificationsystem.utils.ResponseMessage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/result")
public class StudentResultController {

    private final StudentResultService studentResultService;

    @PostMapping(value = "/add")
    public ResponseEntity<ResponseMessage> addNewResult(
            @RequestBody @Valid NewStudentResultDto newStudentResultDto) {

        studentResultService.addNewStudentResult(newStudentResultDto);

        return ResponseEntity.ok(new ResponseMessage("SUCCESS", 0, Map.of("courses", newStudentResultDto.courseScore())));
    }

    @GetMapping(value = "/get/{matric}")
    public ResponseEntity<ResponseMessage> getStudentResult(
            @PathVariable @NotBlank String matric,
            @RequestParam(value = "semester", required = false) String semester,
            @RequestParam(value = "session", required = false) String session)  {

        StudentResultDto studentResult = studentResultService.getStudentResult(matric, Optional.ofNullable(semester), Optional.ofNullable(session));
        return ResponseEntity.ok(new ResponseMessage("SUCCESS", 0, Map.of("result", studentResult)));

    }

    @PostMapping(value = "/handle", consumes = APPLICATION_FORM_URLENCODED_VALUE)
    public String handleUssdRequest(UssdRequest ussdRequest) {

          return studentResultService.handleUssdRequest(ussdRequest);
    }

}
