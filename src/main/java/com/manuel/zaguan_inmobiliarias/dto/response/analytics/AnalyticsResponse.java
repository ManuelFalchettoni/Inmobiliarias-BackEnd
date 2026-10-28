package com.manuel.zaguan_inmobiliarias.dto.response.analytics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//Todo lo de la pantalla de Analitica en un solo response: el front no cuenta nada
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {
    private PropertyAnalyticsResponse properties;
    private CrmAnalyticsResponse crm;
    private ContractAnalyticsResponse contracts;
    private List<AgentAnalyticsResponse> agents;
}
