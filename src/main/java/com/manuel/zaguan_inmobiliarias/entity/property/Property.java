package com.manuel.zaguan_inmobiliarias.entity.property;

import com.manuel.zaguan_inmobiliarias.enums.property.PropertySituation;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyStatus;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Property{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private List<ProductPhoto> photos = new ArrayList<>();
}
