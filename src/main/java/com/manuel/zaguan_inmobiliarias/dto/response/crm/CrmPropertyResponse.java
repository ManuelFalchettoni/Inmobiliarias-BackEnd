package com.manuel.zaguan_inmobiliarias.dto.response.crm;

import com.manuel.zaguan_inmobiliarias.enums.crm.CrmStage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrmPropertyResponse {
    private Long id;
    private Long propertyId;
    private Long peopleId;
    private Long userId;
    private CrmStage stage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
