package com.manuel.zaguan_inmobiliarias.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Solo para PATCH /api/users/{id}/password
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordRequest {
    @NotBlank
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    private String password;
}
