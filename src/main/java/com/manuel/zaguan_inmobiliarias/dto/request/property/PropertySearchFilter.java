package com.manuel.zaguan_inmobiliarias.dto.request.property;

import com.manuel.zaguan_inmobiliarias.enums.Currency;
import com.manuel.zaguan_inmobiliarias.enums.property.OperationType;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyCondition;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyOccupancy;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

//Los filtros de GET /api/properties/search, que Spring arma desde los query params.
//Todos opcionales: el que llega en null no filtra.
//type, condition y occupancy aceptan varios valores: type=HOUSE,PH o type=HOUSE&type=PH
@Getter @Setter
@NoArgsConstructor
public class PropertySearchFilter {

    //Lo respeta solo el admin (AgencyScopeService): una inmobiliaria busca siempre en la suya
    private Long agencyId;

    private List<PropertyType> type;
    private List<PropertyCondition> condition;
    private List<PropertyOccupancy> occupancy;

    private String address;
    private String province;
    private String county;
    private String city;

    private Integer minRooms;
    private Integer maxRooms;
    private Integer minSize;
    private Integer maxSize;
    private Integer minYear;
    private Integer maxYear;
    private Integer minFloor;
    private Integer maxFloor;

    //Recuadro del mapa: las propiedades sin coordenadas quedan afuera
    private Double minLat;
    private Double maxLat;
    private Double minLng;
    private Double maxLng;

    private OperationType operationType;
    private Currency currency;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
