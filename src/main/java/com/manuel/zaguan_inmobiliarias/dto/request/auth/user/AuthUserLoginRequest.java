package com.manuel.zaguan_inmobiliarias.dto.request.auth.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserLoginRequest {
    @NotBlank
    @Email(message = "Email should be valid.")
    private String email;

    @NotBlank
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    private String password;

    private boolean remember;


}
