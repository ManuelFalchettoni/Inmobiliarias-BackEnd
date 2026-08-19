package com.manuel.zaguan_inmobiliarias.dto.request.property;

import com.manuel.zaguan_inmobiliarias.enums.property.PropertySituation;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyStatus;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PropertyRequest {

    @NotBlank
    private String address;

    @NotNull
    private PropertyType type;

    @NotBlank
    private String location;

    @NotNull
    private Long idAgency;

    @PastOrPresent
    private LocalDate year;

    @PositiveOrZero
    private int rooms;

    @Positive
    private int size;

    @NotNull
    private PropertySituation situation;

    @NotNull
    private PropertyStatus status;

    @PositiveOrZero
    private int floorNumber;
}
