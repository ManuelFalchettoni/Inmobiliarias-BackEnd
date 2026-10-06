package com.manuel.zaguan_inmobiliarias.dto.response.crm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrmAlertResponse {
    private Long id;
    private Long crmPropertyId;
    private Long userId;
    private String message;
    private LocalDateTime alertDate;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
