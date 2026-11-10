package com.manuel.zaguan_inmobiliarias.entity.auth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

//Un pedido de "olvide mi contraseña". El link del mail lleva el token; aca se guarda solo su
//hash SHA-256, asi alguien que lee la base no puede usar los links que estan vigentes
@Entity
@Table(name = "password_reset_tokens")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    //SHA-256 en hexadecimal: 64 caracteres
    @Column(nullable = false, unique = true, length = 64)
    private String tokenHash;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    //Cuando se uso para cambiar la contraseña (o se invalido por pedir otro). Uno usado no sirve mas
    @Column
    private LocalDateTime usedAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
