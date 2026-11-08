package com.manuel.zaguan_inmobiliarias.dto.request.auth.password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//POST /api/auth/password/forgot
public record PasswordForgotRequest(
        @NotBlank @Email(message = "Email should be valid.") String email
) {}
