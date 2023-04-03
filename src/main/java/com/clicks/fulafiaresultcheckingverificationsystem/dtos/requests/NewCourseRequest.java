package com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewCourseRequest(

        @NotBlank(message = "Course title required")
        String title,

        @NotBlank(message = "Course code required")
        String code,

        @NotNull(message = "Course unit required")
        int unit,

        @NotBlank(message = "Course semester required")
        String semester,

        @NotBlank(message = "Course level required")
        String level
) {
}
