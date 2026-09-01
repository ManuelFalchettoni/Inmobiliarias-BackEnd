package com.manuel.zaguan_inmobiliarias.entity.agency;

import com.manuel.zaguan_inmobiliarias.enums.agency.AgencyStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "agencies")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, unique = true)
    @Size(min = 9, max = 13)
    private String cuit;

    @Column (nullable = false, unique = true)
    @Size(min = 3, max = 30)
    private String companyName;

    @Column(nullable = false)
    @Size(min = 3, max = 30)
    private String publicName;

    @Column(nullable = false, unique = true)
    @Email
    @Size(min = 3, max = 20)
    private String email;

    @Column(nullable = false)
    @Size(min = 8, max = 20)
    private String password;

    @Column(nullable = false, unique = true)
    @Size(min = 8, max = 15)
    private String phoneNumber;

    @Column (nullable = false, unique = true)
    @Size(min = 6, max = 40)
    private String address;

    @Column (unique = true)
    private String webURL;

    @Column (unique = true)
    private String socials;

    @Column (nullable = false)
    @Enumerated(EnumType.STRING)
    private AgencyStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
