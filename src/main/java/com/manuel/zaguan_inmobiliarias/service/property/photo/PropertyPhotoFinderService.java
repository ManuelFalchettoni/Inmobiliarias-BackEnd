package com.manuel.zaguan_inmobiliarias.service.property.photo;

import com.manuel.zaguan_inmobiliarias.dto.response.property.photo.PropertyPhotoResponse;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.photo.PropertyPhotoMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.photo.JpaPropertyPhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PropertyPhotoFinderService {
    private final JpaPropertyPhotoRepository jpaPropertyPhotoRepository;
    private final JpaPropertyRepository jpaPropertyRepository;
    private final PropertyPhotoMapper propertyPhotoMapper;

    public PropertyPhotoFinderService (JpaPropertyRepository jpaPropertyRepository, PropertyPhotoMapper propertyPhotoMapper, JpaPropertyPhotoRepository jpaPropertyPhotoRepository){
        this.jpaPropertyRepository = jpaPropertyRepository;
        this.propertyPhotoMapper = propertyPhotoMapper;
        this.jpaPropertyPhotoRepository = jpaPropertyPhotoRepository;
    }

    public List<PropertyPhotoResponse> findByProperty(Long propertyId) {
        jpaPropertyRepository.findByIdAndActiveTrue(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));

        return jpaPropertyPhotoRepository.findByPropertyIdOrderByPositionAsc(propertyId)
                .stream()
                .map(propertyPhotoMapper::toResponse)
                .toList();
    }


}
