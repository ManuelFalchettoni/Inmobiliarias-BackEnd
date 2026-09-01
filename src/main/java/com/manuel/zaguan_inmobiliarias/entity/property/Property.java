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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    //El largo tiene que coincidir con el @Size de PropertyRequest, si no la validacion
    //deja pasar textos que despues MySQL rechaza
    @Column(length = 150)
    private String address;

    @Column(nullable = false)
    private Boolean active;

    //columnDefinition varchar y no el ENUM nativo que Hibernate genera por defecto en MySQL:
    //con ddl-auto=update la columna ENUM no se modifica, asi que agregar una constante
    //nueva al enum rompe los inserts
    @Column(nullable = false, columnDefinition = "varchar(30)")
    @Enumerated(EnumType.STRING)
    private PropertyType type;

    @Column(length = 100)
    private String location;

    @Column
    private Long idAgency;

    @Column(name = "construction_year")
    private Integer year;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column
    private int rooms;

    @Column
    private int size;

    @Column(name = "property_condition", nullable = false, columnDefinition = "varchar(30)")
    @Enumerated(EnumType.STRING)
    private PropertyCondition condition;

    @Column(nullable = false, columnDefinition = "varchar(30)")
    @Enumerated(EnumType.STRING)
    private PropertyOccupancy occupancy;

    @Column
    private int floorNumber;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropertyPhoto> photos = new ArrayList<>();
}
