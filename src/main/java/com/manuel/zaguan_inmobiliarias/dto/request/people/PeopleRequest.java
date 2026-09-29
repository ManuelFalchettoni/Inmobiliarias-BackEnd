package com.manuel.zaguan_inmobiliarias.dto.request.people;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeopleRequest {

    @NotBlank
    @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters.")
    private String name;

    @NotBlank
    @Email
    @Size(max = 100, message = "Email must be at most 100 characters.")
    private String email;

    @NotBlank
    @Size(min = 8, max = 15, message = "Phone must be between 8 and 15 characters.")
    private String phone;

    @Size(min = 5, max = 150, message = "Address must be between 8 and 150 characters.")
    private String address;

    @NotBlank
    @Size(min = 6, max = 10, message = "DNI must be between 6 and 10 characters.")
    private String dni;

    @NotBlank
    @Size(min = 11, max = 13, message = "Cuit must be between 11 and 13 characters.")
    private String cuit;

}
