package com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests;

import com.clicks.fulafiaresultcheckingverificationsystem.dtos.CourseScore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record NewStudentResultDto(

        @NotBlank(message = "Student matric field required")
        String matric,

        @NotEmpty(message = "courses and score required")
        List<CourseScore> courseScore
) {
}
