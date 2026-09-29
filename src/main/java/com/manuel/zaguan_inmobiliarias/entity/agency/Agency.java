package com.manuel.zaguan_inmobiliarias.entity.agency;

import com.manuel.zaguan_inmobiliarias.enums.agency.AgencyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

//Las validaciones estan en AgencyRequest. Aca solo el largo de las columnas, que tiene que
//coincidir con el @Size del request
@Entity
@Table(name = "agencies")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, unique = true, length = 13)
    private String cuit;

    @Column (nullable = false, unique = true, length = 30)
    private String companyName;

    @Column(nullable = false, length = 30)
    private String publicName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    //Se guarda el hash de BCrypt (60 caracteres), no la contraseña
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Column (nullable = false, unique = true, length = 40)
    private String address;

    //Opcionales y sin unique: varias sucursales pueden compartir la misma web o redes
    @Column(length = 255)
    private String webURL;

    @Column(length = 255)
    private String socials;

    //Baja logica: la inmobiliaria no se borra nunca de la base. Property.idAgency es un id
    //suelto, sin FK, asi que un borrado fisico dejaria propiedades apuntando a la nada
    @Column(nullable = false)
    private Boolean active;

    //status es el estado de verificacion, no el alta/baja: una inmobiliaria dada de baja
    //conserva el status que tenia
    //columnDefinition varchar y no el ENUM nativo que Hibernate genera por defecto en MySQL:
    //con ddl-auto=update la columna ENUM no se modifica, asi que agregar una constante
    //nueva al enum rompe los inserts
    @Column (nullable = false, columnDefinition = "varchar(30)")
    @Enumerated(EnumType.STRING)
    private AgencyStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
