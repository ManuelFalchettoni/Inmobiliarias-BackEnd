package com.manuel.zaguan_inmobiliarias.service.property;

import com.manuel.zaguan_inmobiliarias.dto.request.property.PropertyRequest;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import org.springframework.stereotype.Service;

@Service
public class PropertyCreatorService {
    private final JpaPropertyRepository jpaPropertyRepository;

    public PropertyCreatorService (JpaPropertyRepository jpaPropertyRepository){
        this.jpaPropertyRepository = jpaPropertyRepository;
    }

    public Property create(PropertyRequest request){
        Property property = PropertyRequest.fromRequest(request);
        return jpaPropertyRepository.save(property);
    }
}
