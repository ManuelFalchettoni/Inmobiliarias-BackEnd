package com.manuel.zaguan_inmobiliarias.dto.response.crm;

import com.manuel.zaguan_inmobiliarias.dto.response.people.PeopleSummaryResponse;
import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertySummaryResponse;
import com.manuel.zaguan_inmobiliarias.dto.response.user.UserSummaryResponse;
import com.manuel.zaguan_inmobiliarias.enums.crm.CrmStage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrmPropertyResponse {
    private Long id;
    private Long propertyId;
    //En null solo si la propiedad ya no existe en la base
    private PropertySummaryResponse property;
    private Long peopleId;
    //En null solo si la persona ya no existe en la base
    private PeopleSummaryResponse person;
    private Long userId;
    //El agente asignado. En null solo si el usuario ya no existe en la base
    private UserSummaryResponse agent;
    private CrmStage stage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
