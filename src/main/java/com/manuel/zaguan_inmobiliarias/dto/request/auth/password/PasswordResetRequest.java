package com.manuel.zaguan_inmobiliarias.dto.request.auth.password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//POST /api/auth/password/reset. El token es el que llega en el link del mail
public record PasswordResetRequest(
        @NotBlank String token,
        @NotBlank @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.") String password
) {}
