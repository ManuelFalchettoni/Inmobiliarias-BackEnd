package com.manuel.zaguan_inmobiliarias.service.property;

import com.manuel.zaguan_inmobiliarias.dto.request.property.PropertyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertyResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.mapper.property.PropertyMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertyCreatorService {
    private final JpaPropertyRepository jpaPropertyRepository;
    private final PropertyMapper propertyMapper;

    public PropertyCreatorService (JpaPropertyRepository jpaPropertyRepository, PropertyMapper propertyMapper){
        this.jpaPropertyRepository = jpaPropertyRepository;
        this.propertyMapper = propertyMapper;
    }

    @Transactional
    public PropertyResponse create(PropertyRequest request){
        Property property = jpaPropertyRepository.save(propertyMapper.toEntity(request));
        return propertyMapper.toResponse(property);
    }
}
