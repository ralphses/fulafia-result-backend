package com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.auth;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record PasswordModel(
        @NotBlank(message = "Email field required")
        String email,

        @Length(min = 8, message = "Password must not be less than 8 characters")
        @NotBlank(message = "New password field required")
        String newPassword,

        @NotBlank(message = "New password field required")
        String confirmPassword

) {
}
