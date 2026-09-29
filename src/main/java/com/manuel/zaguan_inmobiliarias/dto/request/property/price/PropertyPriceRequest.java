package com.manuel.zaguan_inmobiliarias.dto.request.property.price;

import com.manuel.zaguan_inmobiliarias.enums.Currency;
import com.manuel.zaguan_inmobiliarias.enums.property.OperationType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PropertyPriceRequest {

    @NotNull
    private OperationType operationType;

    @NotNull
    private Currency currency;

    //Digits tiene que coincidir con precision = 15, scale = 2 de la columna
    @NotNull
    @Positive
    @Digits(integer = 13, fraction = 2)
    private BigDecimal amount;
}
