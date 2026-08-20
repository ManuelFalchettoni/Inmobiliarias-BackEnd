package com.manuel.zaguan_inmobiliarias.dto.response.property;

import com.manuel.zaguan_inmobiliarias.dto.response.property.photo.PropertyPhotoResponse;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyCondition;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyOccupancy;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private Integer year;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int rooms;

    private int size;

    private PropertyCondition condition;

    private PropertyOccupancy occupancy;

    private int floorNumber;

    private List<PropertyPhotoResponse> photos = new ArrayList<>();
}
