package com.manuel.zaguan_inmobiliarias.entity.agency;

import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.enums.agency.AgencyStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Size(min = 3, max = 30)
    private String companyName;

    @Column(nullable = false)
    @Size(min = 3, max = 30)
    private String publicName;

    @OneToOne
    @JoinColumn(name = "Owner_id")
    private User user;

    @Column (nullable = false, unique = true)
    @Size(min = 6, max = 40)
    private String address;

    @Column (unique = true)
    private String webURL;

    @Column (unique = true)
    private String socials;

    @Column (nullable = false)
    private AgencyStatus status;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

}
