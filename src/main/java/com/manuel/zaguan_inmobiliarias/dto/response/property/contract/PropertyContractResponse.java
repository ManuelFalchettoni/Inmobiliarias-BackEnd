package com.manuel.zaguan_inmobiliarias.dto.response.property.contract;

import com.manuel.zaguan_inmobiliarias.dto.response.contractparty.ContractPartyResponse;
import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertySummaryResponse;
import com.manuel.zaguan_inmobiliarias.enums.Currency;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractStatus;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyContractResponse {
    private Long id;
    private Long propertyId;
    //En null solo si la propiedad ya no existe en la base
    private PropertySummaryResponse property;
    //Las partes del contrato, cada una con su persona. Lista vacia si todavia no tiene
    private List<ContractPartyResponse> parties;
    private ContractType type;
    private ContractStatus status;
    private BigDecimal amount;
    private Currency currency;
    private LocalDate startDate;
    private LocalDate endDate;
    private String documentURL;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
