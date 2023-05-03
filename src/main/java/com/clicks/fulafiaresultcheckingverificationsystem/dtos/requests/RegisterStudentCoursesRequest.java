package com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record RegisterStudentCoursesRequest(
        String matric,
        @NotEmpty(message = "Courses required")
        List<String> courses
) {
}
