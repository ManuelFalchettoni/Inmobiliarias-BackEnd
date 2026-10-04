package com.manuel.zaguan_inmobiliarias.dto.response.property.owner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyOwnerResponse {
    private Long id;
    private Long propertyId;
    private Long peopleId;
    private String comments;
}
