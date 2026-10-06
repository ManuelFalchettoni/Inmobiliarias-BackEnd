package com.manuel.zaguan_inmobiliarias.dto.request.property.owner;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyOwnerRequest {
    @NotNull
    private Long propertyId;

    @NotNull
    private Long peopleId;

    @NotBlank
    private String comments;
}
