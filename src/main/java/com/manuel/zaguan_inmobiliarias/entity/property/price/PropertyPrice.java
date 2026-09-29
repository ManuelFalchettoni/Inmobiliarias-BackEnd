package com.manuel.zaguan_inmobiliarias.entity.property.price;

import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.enums.Currency;
import com.manuel.zaguan_inmobiliarias.enums.property.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

//Un precio por operacion: una propiedad puede estar en venta y en alquiler a la vez,
//pero no tener dos precios de venta. El unique lo controla antes PropertyPriceCreatorService
@Entity
@Table(name = "property_prices",
        uniqueConstraints = @UniqueConstraint(columnNames = {"property_id", "operation_type"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PropertyPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //varchar y no ENUM
    @Column(name = "operation_type", nullable = false, columnDefinition = "varchar(30)")
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @Column(nullable = false, columnDefinition = "varchar(10)")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    //BigDecimal y no Double: con plata no se quieren errores de redondeo
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
}
