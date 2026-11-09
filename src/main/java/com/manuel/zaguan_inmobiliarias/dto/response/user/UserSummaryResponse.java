package com.manuel.zaguan_inmobiliarias.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Lo minimo del agente para mostrarlo dentro de un lead
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryResponse {
    private Long id;
    private String name;
}
