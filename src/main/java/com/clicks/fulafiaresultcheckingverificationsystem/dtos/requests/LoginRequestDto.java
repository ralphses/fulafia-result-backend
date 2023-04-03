package com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "Email field required")
        String email,
        @NotBlank(message = "Password field required")
        String password
) {
}
