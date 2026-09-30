package com.manuel.zaguan_inmobiliarias.dto.response.property.contract;

import com.manuel.zaguan_inmobiliarias.enums.Currency;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractStatus;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PropertyContractResponse {
    private Long id;
    private Long propertyId;
    private ContractType type;
    private ContractStatus status;
    private BigDecimal amount;
    private Currency currency;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String documentURl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
