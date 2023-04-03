package com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Map;

public record NewStudentResultDto(

        @NotBlank(message = "Student matric field required")
        String matric,

        @NotEmpty(message = "courses and score required")
        Map<String, String> courseScore
) {
}
