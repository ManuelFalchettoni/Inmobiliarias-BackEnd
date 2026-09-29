package com.manuel.zaguan_inmobiliarias.dto.response.property.price;

import com.manuel.zaguan_inmobiliarias.enums.Currency;
import com.manuel.zaguan_inmobiliarias.enums.property.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PropertyPriceResponse {
    private Long id;

    private OperationType operationType;

    private Currency currency;

    private BigDecimal amount;
}
