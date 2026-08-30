package com.manuel.zaguan_inmobiliarias.dto.response.user;

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

    private int phoneNumber;

    private String rol;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public UserResponse(Long id, String name, String email, String rol){
        this.id = id;
        this.name = name;
        this.email = email;
        this.rol = rol;
    }

}
