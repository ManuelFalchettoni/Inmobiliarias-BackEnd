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

    private String surname;

    private String email;

    private boolean active;

    private UserRol rol;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
