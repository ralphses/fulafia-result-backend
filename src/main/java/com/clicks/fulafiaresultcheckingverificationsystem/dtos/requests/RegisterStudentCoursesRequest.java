package com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RegisterStudentCoursesRequest(

        @NotBlank(message = "Course semester required")
        String semester,

        @NotNull(message = "Course level required")
        Integer level,

        @NotEmpty(message = "Courses required")
        List<String> courses
) {
}
