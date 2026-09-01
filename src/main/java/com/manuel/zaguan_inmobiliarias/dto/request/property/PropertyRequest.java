package com.manuel.zaguan_inmobiliarias.dto.request.property;

import com.manuel.zaguan_inmobiliarias.enums.property.PropertyCondition;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyOccupancy;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PropertyRequest {

    @NotBlank
    @Size(max = 150)
    private String address;

    @NotNull
    private PropertyType type;

    @NotBlank
    @Size(max = 100)
    private String location;

    @NotNull
    private Long idAgency;

    @Min(1800)
    @Max(2100)
    private Integer year;

    @PositiveOrZero
    private int rooms;

    @Positive
    private int size;

    @NotNull
    private PropertyCondition condition;

    @NotNull
    private PropertyOccupancy occupancy;

    @PositiveOrZero
    private int floorNumber;
}
