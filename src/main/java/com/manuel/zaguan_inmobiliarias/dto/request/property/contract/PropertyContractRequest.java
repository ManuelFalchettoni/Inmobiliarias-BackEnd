package com.manuel.zaguan_inmobiliarias.dto.request.property.contract;

import com.manuel.zaguan_inmobiliarias.enums.Currency;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractStatus;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyContractRequest {
    @NotBlank
    private ContractType type;

    @NotBlank
    private ContractStatus status;

    @NotNull
    @Positive(message = "Amount must be a positive number.")
    @Digits(integer = 15, fraction = 2, message = "Amount format is not valid.")
    private BigDecimal amount;

    @NotBlank
    private Currency currency;

    @NotBlank
    private LocalDateTime startDate;

    @NotBlank
    private LocalDateTime endDate;

    @NotBlank
    private String documentURL;
}
