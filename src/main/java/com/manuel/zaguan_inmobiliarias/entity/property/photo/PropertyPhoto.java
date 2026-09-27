package com.manuel.zaguan_inmobiliarias.entity.property.photo;

import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "property_photos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PropertyPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Solo el nombre del archivo en MinIO (uuid.jpg), no la URL completa: la URL se arma al
    //responder
    @Column(nullable = false, length = 50)
    private String objectKey;

    //Nombre original del archivo.
    @Column(length = 255)
    private String photoName;

    @Column(name = "photo_position", nullable = false)
    private int position;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
}
