package com.manuel.zaguan_inmobiliarias.dto.response.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Lo minimo de la propiedad para mostrarla dentro de un contrato o un lead. Va ahi adentro porque
//GET /api/properties/{id} responde 404 para las archivadas, y contratos y leads las siguen necesitando
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertySummaryResponse {
    private Long id;
    private String address;
    private String city;
    private String county;
    private String province;
    private Boolean active;
}
