package com.manuel.zaguan_inmobiliarias.dto.response.crm;

import com.manuel.zaguan_inmobiliarias.enums.crm.CrmStage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Una fila de la agenda: un lead con el aviso que le toca mostrar
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrmAgendaResponse {
    //El id del lead
    private Long id;
    private Long peopleId;
    private Long propertyId;
    private Long userId;
    private CrmStage stage;
    private String personName;
    private String personPhone;
    private String propertyAddress;
    //El vencido mas atrasado o, si no hay, el proximo pendiente. null si el lead no tiene avisos
    private CrmAlertResponse nextAlert;
    //Todos los PENDING del lead, vencidos y por delante
    private Integer pendingCount;
    //Si nextAlert esta vencido
    private Boolean overdue;
    //overdue, hour, day, week, later o null si no tiene avisos
    private String bucket;
}
