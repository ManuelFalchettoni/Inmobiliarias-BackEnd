package com.manuel.zaguan_inmobiliarias.dto.response.analytics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentAnalyticsResponse {
    private Long userId;
    private String name;
    private Long leads;
    private Long closedLeads;
    private Long expiredAlerts;
}
