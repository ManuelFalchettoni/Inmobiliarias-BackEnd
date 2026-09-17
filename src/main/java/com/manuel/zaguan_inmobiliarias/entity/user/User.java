package com.manuel.zaguan_inmobiliarias.entity.user;

import com.manuel.zaguan_inmobiliarias.enums.user.UserRol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

//Las validaciones estan en UserRequest. Aca solo el largo de las columnas, que tiene que
//coincidir con el @Size del request
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    //Se guarda el hash de BCrypt (60 caracteres), no la contraseña
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 15)
    private String phoneNumber;

    //Baja logica: el delete lo pone en false, la fila no se borra
    @Column(nullable = false)
    private boolean active;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    //columnDefinition varchar y no el ENUM nativo que Hibernate genera por defecto en MySQL:
    //con ddl-auto=update la columna ENUM no se modifica, asi que agregar una constante
    //nueva al enum rompe los inserts
    @Column(nullable = false, columnDefinition = "varchar(30)")
    @Enumerated (EnumType.STRING)
    private UserRol rol;

}
