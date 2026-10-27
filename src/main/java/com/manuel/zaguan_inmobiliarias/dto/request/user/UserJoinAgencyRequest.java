package com.manuel.zaguan_inmobiliarias.dto.request.user;

import com.manuel.zaguan_inmobiliarias.enums.user.UserRol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Solo para POST /api/users/join-agency: suma a una inmobiliaria a alguien que ya se registro
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinAgencyRequest {

    //El email con el que se registro. Se lo pide la inmobiliaria a la persona
    @NotBlank
    @Email(message = "Email should be valid.")
    private String email;

    //AGENT o AGENCY. El service rechaza USER y ADMIN
    @NotNull
    private UserRol rol;

    //Solo lo usa el admin. Una inmobiliaria siempre suma a la suya, mande lo que mande
    private Long agencyId;
}
