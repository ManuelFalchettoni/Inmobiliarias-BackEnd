package com.manuel.zaguan_inmobiliarias.entity.property.Owner;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "property_owners")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyOwner {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long propertyId;

    @Column(nullable = false)
    private Long peopleId;

    @Column
    private String comments;
}
