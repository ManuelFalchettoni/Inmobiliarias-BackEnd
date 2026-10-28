package com.manuel.zaguan_inmobiliarias.entity.crm;

import com.manuel.zaguan_inmobiliarias.enums.crm.CrmAlertStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "crm_alerts")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrmAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long crmPropertyId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private LocalDateTime alertDate;

    //PENDING hasta que el agente la da por terminada. Vencida no se guarda: depende de la hora,
    //se calcula en CrmAlertMapper. varchar y no ENUM nativo, por lo mismo que en CrmProperty
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(30)")
    private CrmAlertStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
