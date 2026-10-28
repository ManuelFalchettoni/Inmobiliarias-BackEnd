package com.manuel.zaguan_inmobiliarias.dto.response.analytics;

import com.manuel.zaguan_inmobiliarias.enums.crm.CrmStage;
import com.manuel.zaguan_inmobiliarias.enums.crm.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrmAnalyticsResponse {
    private Long totalLeads;
    private Map<CrmStage, Long> leadsByStage;
    //Porcentaje de CLOSED sobre los terminados (CLOSED + DISCARDED). 0 si no hay terminados
    private Double closeRate;
    private Map<OfferStatus, Long> offersByStatus;
    private Long pendingAlerts;
    private Long expiredAlerts;
}
