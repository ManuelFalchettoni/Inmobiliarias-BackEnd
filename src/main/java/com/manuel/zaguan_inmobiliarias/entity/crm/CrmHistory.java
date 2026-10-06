package com.manuel.zaguan_inmobiliarias.entity.crm;

import com.manuel.zaguan_inmobiliarias.enums.crm.CrmEventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "crm_history")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrmHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long crmPropertyId;

    //El agente que registro el evento: puede no ser el asignado al lead
    @Column(nullable = false)
    private Long userId;

    //varchar y no ENUM nativo, por lo mismo que en User: con ddl-auto=update agregar
    //una constante nueva al enum romperia los inserts
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(30)")
    private CrmEventType type;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String comments;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
