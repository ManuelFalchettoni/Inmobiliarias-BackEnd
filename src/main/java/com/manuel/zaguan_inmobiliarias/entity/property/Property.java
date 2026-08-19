package com.manuel.zaguan_inmobiliarias.entity.property;

import com.manuel.zaguan_inmobiliarias.entity.property.photo.PropertyPhoto;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyCondition;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyOccupancy;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "properties")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Property{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String address;

    @Column
    private Boolean active;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PropertyType type;

    @Column
    private String location;

    @Column
    private Long idAgency;

    @Column
    private LocalDate year;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private int rooms;

    @Column
    private int size;

    @Column(name = "property_condition", nullable = false)
    @Enumerated(EnumType.STRING)
    private PropertyCondition condition;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PropertyOccupancy occupancy;

    @Column
    private int floorNumber;

    @Column
    @OneToMany
    private List<PropertyPhoto> photos = new ArrayList<>();
}
