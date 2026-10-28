package com.manuel.zaguan_inmobiliarias.dto.response.crm;

import com.manuel.zaguan_inmobiliarias.enums.crm.CrmAlertStatus;
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
    private CrmAlertStatus status;
    //Pendiente y con la fecha pasada. Lo calcula el back para no depender de la hora del navegador
    private Boolean expired;
    private LocalDateTime createdAt;
}
