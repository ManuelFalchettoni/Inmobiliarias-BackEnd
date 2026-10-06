package com.manuel.zaguan_inmobiliarias.dto.request.crm;

import com.manuel.zaguan_inmobiliarias.enums.crm.CrmStage;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Mismo body para POST y PUT
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrmPropertyRequest {
    @NotNull
    private Long propertyId;

    @NotNull
    private Long peopleId;

    //El agente asignado al lead
    @NotNull
    private Long userId;

    @NotNull
    private CrmStage stage;
}
