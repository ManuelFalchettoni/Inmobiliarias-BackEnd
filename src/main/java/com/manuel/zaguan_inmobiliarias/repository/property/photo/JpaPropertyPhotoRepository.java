package com.manuel.zaguan_inmobiliarias.repository.property.photo;

import com.manuel.zaguan_inmobiliarias.entity.property.photo.PropertyPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPropertyPhotoRepository extends JpaRepository<PropertyPhoto, Long> {

    List<PropertyPhoto> findByPropertyIdOrderByPositionAsc(Long propertyId);

    Optional<PropertyPhoto> findByIdAndPropertyId(Long propertyPhotoId, Long propertyId);

    int countByPropertyId(Long propertyId);

}
