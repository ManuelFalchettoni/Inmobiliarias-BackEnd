package com.manuel.zaguan_inmobiliarias.entity.crm;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "crm_alert")
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

    @Column(nullable = true, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private LocalDateTime alertDate;

    @Column(nullable = false)
    private Boolean isRead;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
