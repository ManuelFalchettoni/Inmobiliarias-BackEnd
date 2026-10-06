package com.manuel.zaguan_inmobiliarias.entity.people;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
//Cada inmobiliaria tiene su propia cartera: la misma persona puede estar cargada en dos
//inmobiliarias distintas, pero no dos veces en la misma. El DNI acepta null y MySQL
//permite varios null en un unique, asi que los interesados sin DNI no chocan
@Table(name = "people",
        uniqueConstraints = @UniqueConstraint(columnNames = {"agency_id", "dni"}))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class People {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(nullable = false, length = 100)
    @Email
    private String email;

    @Column(length = 150)
    private String address;

    //DNI y CUIT opcionales: un interesado del CRM se carga con nombre y telefono
    @Column(length = 10)
    private String dni;

    @Column(length = 13)
    private String cuit;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "agency_id", nullable = false)
    private Long agencyId;



}
