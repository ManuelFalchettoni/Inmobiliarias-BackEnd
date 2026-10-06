package com.manuel.zaguan_inmobiliarias.dto.response.user;

import com.manuel.zaguan_inmobiliarias.enums.user.UserRol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private boolean active;

    private String phoneNumber;

    private UserRol rol;

    private Long idAgency;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long agencyId;

    private String cuit;

    private String license;


    public UserResponse(Long id, String name, String email, UserRol rol){
        this.id = id;
        this.name = name;
        this.email = email;
        this.rol = rol;
    }

}
