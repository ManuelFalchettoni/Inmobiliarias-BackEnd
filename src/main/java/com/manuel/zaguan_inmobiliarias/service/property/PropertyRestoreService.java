package com.manuel.zaguan_inmobiliarias.service.property;

import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertyResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.PropertyMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertyRestoreService {
    private final JpaPropertyRepository jpaPropertyRepository;
    private final PropertyMapper propertyMapper;

    public PropertyRestoreService (JpaPropertyRepository jpaPropertyRepository, PropertyMapper propertyMapper){
        this.jpaPropertyRepository = jpaPropertyRepository;
        this.propertyMapper = propertyMapper;
    }

    @Transactional
    public PropertyResponse restore(Long id){
        //findById pelado:Si la propiedad ya estaba activa no hace nada
        //y devuelve 200 igual
        Property property = jpaPropertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException(id));

        property.setActive(true);

        //Flush para que updatedAt salga actualizado en la respuesta
        jpaPropertyRepository.saveAndFlush(property);
        return propertyMapper.toResponse(property);
    }
}
