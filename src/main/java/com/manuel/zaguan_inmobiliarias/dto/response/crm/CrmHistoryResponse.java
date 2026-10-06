package com.manuel.zaguan_inmobiliarias.dto.response.crm;

import com.manuel.zaguan_inmobiliarias.enums.crm.CrmEventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrmHistoryResponse {
    private Long id;
    private Long crmPropertyId;
    private Long userId;
    private CrmEventType type;
    private String comments;
    private LocalDateTime createdAt;
}
