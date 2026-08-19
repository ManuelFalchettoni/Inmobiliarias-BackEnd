package com.manuel.zaguan_inmobiliarias.dto.response.property;

import com.manuel.zaguan_inmobiliarias.enums.property.PropertySituation;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyStatus;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PropertyResponse {

    private Long id;

    private String address;

    private Boolean active;

    private PropertyType type;

    private String location;

    private Long idAgency;

    private LocalDate year;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int rooms;

    private int size;

    private PropertySituation situation;

    private PropertyStatus status;

    private int floorNumber;

    private List<String> photoUrls = new ArrayList<>();
}
