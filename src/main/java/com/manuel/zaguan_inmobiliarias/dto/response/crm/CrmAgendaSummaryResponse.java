package com.manuel.zaguan_inmobiliarias.dto.response.crm;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Cuantos leads hay en cada tramo de la agenda
@Getter @Setter
@NoArgsConstructor
public class CrmAgendaSummaryResponse {
    private long overdue;
    private long hour;
    private long day;
    private long week;
    private long later;
    private long withoutAlerts;
}
