package com.manuel.zaguan_inmobiliarias.dto.request.user;

import com.manuel.zaguan_inmobiliarias.enums.user.UserRol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank
    @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters.")
    private String name;

    @NotBlank
    @Email(message = "Email should be valid.")
    private String email;

    @NotBlank
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    private String password;

    @NotBlank
    @Size(min = 8, max = 15, message = "phoneNumber must be between 8 and 15 characters.")
    private int phoneNumber;

    @NotNull
    private UserRol rol;
}
