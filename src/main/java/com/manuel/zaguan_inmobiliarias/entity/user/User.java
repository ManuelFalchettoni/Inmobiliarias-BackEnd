package com.manuel.zaguan_inmobiliarias.entity.user;

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

    @Column
    @Size(min = 3, max = 20)
    private String name;

    @Column
    @Size(min = 3, max = 20)
    private String Surname;

    @Column
    @Email
    @Size(min = 3, max = 20)
    private String email;

    @Column
    @Size(min = 8, max = 20)
    private String password;

    @Column
    @Size(min = 8, max = 15)
    private int phoneNumber;

    @Column
    private boolean active;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime uploadAt;

    @Column
    @Enumerated (EnumType.STRING)
    private UserRol rol;


}
