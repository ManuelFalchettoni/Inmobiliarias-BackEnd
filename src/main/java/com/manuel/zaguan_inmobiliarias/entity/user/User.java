package com.manuel.zaguan_inmobiliarias.entity.user;

import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.enums.user.UserRol;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 3, max = 50)
    private String name;

    @Column(nullable = false, unique = true)
    @Email
    @Size(min = 3, max = 20)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    @Size(min = 8, max = 15)
    private String phoneNumber;

    @Column
    private boolean active;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    @Enumerated (EnumType.STRING)
    private UserRol rol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "agency_id")
    private Agency agency;

    @Column(unique = true)
    @Size(min = 8, max = 30)
    private String license;

    @Column(unique = true)
    @Size(min = 8, max = 20)
    private String cuit;
}
