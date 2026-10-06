package com.manuel.zaguan_inmobiliarias.dto.response.property.contract;

import com.manuel.zaguan_inmobiliarias.enums.Currency;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractStatus;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
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
