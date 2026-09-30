package com.manuel.zaguan_inmobiliarias.dto.response.people;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor

public class PeopleResponse {
    private Long id;

    private Long agencyId;

    private String name;

    private String phone;

    private String email;

    private String address;

    private String dni;

    private String cuit;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}