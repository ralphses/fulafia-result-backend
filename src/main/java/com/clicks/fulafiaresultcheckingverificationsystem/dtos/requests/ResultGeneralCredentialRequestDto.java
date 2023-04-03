package com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record ResultGeneralCredentialRequestDto(

        @NotBlank(message = "Semester required")
        String semester,

        @NotBlank(message = "Session required")
        String session
) {
}
