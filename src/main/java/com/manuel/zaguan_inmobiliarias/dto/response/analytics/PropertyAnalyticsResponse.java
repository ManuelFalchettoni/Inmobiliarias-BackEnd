package com.manuel.zaguan_inmobiliarias.dto.response.analytics;

import com.manuel.zaguan_inmobiliarias.enums.property.OperationType;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

//byType y byOperation cuentan solo las activas
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyAnalyticsResponse {
    private Long active;
    private Long inactive;
    private Map<PropertyType, Long> byType;
    //Una propiedad con precio de venta y de alquiler cuenta en las dos
    private Map<OperationType, Long> byOperation;
}
