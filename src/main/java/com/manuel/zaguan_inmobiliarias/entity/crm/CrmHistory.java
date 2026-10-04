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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CrmEventType type;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String comments;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
