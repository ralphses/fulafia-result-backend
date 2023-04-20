package com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests;

import jakarta.validation.constraints.*;

import java.util.List;

public record NewStudentRequest(

        @NotBlank(message = "Student name field required")
        String name,

        @NotBlank(message = "Student matriculation number field required")
        String matric,

        @NotBlank(message = "Student phone number field required")
        @Size(max = 11, min = 11, message = "Student phone not valid")
        String phone,

        @Email(message = "Enter a valid email address")
        String email,

        @NotBlank(message = "Department field required")
        String department,

        @NotBlank(message = "Level field required")
        String level,

        @NotEmpty(message = "Please select student courses")
        List<String> courses
) {
}
