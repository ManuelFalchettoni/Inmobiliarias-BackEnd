package com.manuel.zaguan_inmobiliarias.dto.response.people;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Lo minimo de la persona para mostrarla dentro de un lead o de una parte de contrato,
//asi el front no tiene que pedir cada persona por separado
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeopleSummaryResponse {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String dni;
}
