package com.manuel.zaguan_inmobiliarias.dto.request.crm;

import com.manuel.zaguan_inmobiliarias.enums.crm.CrmEventType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Solo para el POST: un evento no se edita. El lead sale de la URL, no del body
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrmHistoryRequest {
    //El agente que registra el evento: puede no ser el asignado al lead
    @NotNull
    private Long userId;

    @NotNull
    private CrmEventType type;

    //Opcional. Sin @Size: la columna es TEXT
    private String comments;
}
