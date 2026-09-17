package com.manuel.zaguan_inmobiliarias.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Los mismos campos que UserRequest pero sin password: para cambiar el nombre no hace falta
//reenviar la contraseña. La contraseña se cambia por PATCH /api/users/{id}/password
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @NotBlank
    @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters.")
    private String name;

    @NotBlank
    @Email(message = "Email should be valid.")
    @Size(max = 100, message = "Email must be at most 100 characters.")
    private String email;

    @NotBlank
    @Size(min = 8, max = 15, message = "phoneNumber must be between 8 and 15 characters.")
    private String phoneNumber;
}
