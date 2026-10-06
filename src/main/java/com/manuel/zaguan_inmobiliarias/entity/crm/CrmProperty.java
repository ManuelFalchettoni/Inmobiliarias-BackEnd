package com.manuel.zaguan_inmobiliarias.entity.crm;

import com.manuel.zaguan_inmobiliarias.enums.crm.CrmStage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "crm_properties")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrmProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long propertyId;

    @Column(nullable = false)
    private Long peopleId;

    @Column(nullable = false)
    private Long userId;

    //varchar y no ENUM nativo, por lo mismo que en User: con ddl-auto=update agregar
    //una constante nueva al enum romperia los inserts
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(30)")
    private CrmStage stage;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
