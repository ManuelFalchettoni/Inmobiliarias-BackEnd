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
import java.time.LocalDate;

//@NotBlank es solo para String: en Long, enums o fechas tira excepcion al validar. Para el resto va @NotNull
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyContractRequest {
    @NotNull
    private Long propertyId;

    @NotNull
    private ContractType type;

    @NotNull
    private ContractStatus status;

    @NotNull
    @Positive(message = "Amount must be a positive number.")
    @Digits(integer = 15, fraction = 2, message = "Amount format is not valid.")
    private BigDecimal amount;

    @NotNull
    private Currency currency;

    @NotNull
    private LocalDate startDate;

    //Opcional: un contrato de venta no tiene fecha de fin
    private LocalDate endDate;

    @NotBlank
    private String documentURL;
}
