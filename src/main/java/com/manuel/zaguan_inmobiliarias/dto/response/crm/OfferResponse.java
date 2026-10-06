package com.manuel.zaguan_inmobiliarias.dto.response.crm;

import com.manuel.zaguan_inmobiliarias.enums.Currency;
import com.manuel.zaguan_inmobiliarias.enums.crm.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferResponse {
    private Long id;
    private Long crmPropertyId;
    private BigDecimal amount;
    private Currency currency;
    private OfferStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
