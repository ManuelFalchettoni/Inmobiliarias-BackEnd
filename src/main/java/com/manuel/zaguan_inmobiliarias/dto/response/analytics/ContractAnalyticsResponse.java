package com.manuel.zaguan_inmobiliarias.dto.response.analytics;

import com.manuel.zaguan_inmobiliarias.enums.Currency;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractStatus;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractAnalyticsResponse {
    private Long total;
    private Map<ContractStatus, Long> byStatus;
    private Map<ContractType, Long> byType;
    //Suma de los montos de los ACTIVE, separada por moneda: pesos y dolares no se suman
    private Map<Currency, BigDecimal> activeAmountByCurrency;
}
