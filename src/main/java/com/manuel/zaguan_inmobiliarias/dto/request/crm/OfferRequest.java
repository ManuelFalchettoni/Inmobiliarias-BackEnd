package com.manuel.zaguan_inmobiliarias.dto.request.crm;

import com.manuel.zaguan_inmobiliarias.enums.Currency;
import com.manuel.zaguan_inmobiliarias.enums.crm.OfferStatus;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

//Mismo body para POST y PUT. El lead sale de la URL, no del body
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferRequest {
    //Digits tiene que coincidir con precision = 15, scale = 2 de la columna
    @NotNull
    @Positive
    @Digits(integer = 13, fraction = 2)
    private BigDecimal amount;

    @NotNull
    private Currency currency;

    @NotNull
    private OfferStatus status;
}
