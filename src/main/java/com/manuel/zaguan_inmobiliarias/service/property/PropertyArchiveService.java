package com.manuel.zaguan_inmobiliarias.service.property;

import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertyResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.PropertyMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertyArchiveService {
    private final JpaPropertyRepository jpaPropertyRepository;
    private final PropertyMapper propertyMapper;
    private final AgencyScopeService agencyScopeService;

    public PropertyArchiveService (JpaPropertyRepository jpaPropertyRepository, PropertyMapper propertyMapper,
                                   AgencyScopeService agencyScopeService){
        this.jpaPropertyRepository = jpaPropertyRepository;
        this.propertyMapper = propertyMapper;
        this.agencyScopeService = agencyScopeService;
    }

    @Transactional
    public PropertyResponse archive(Long id){
        //findById pelado, igual que restore: si ya estaba archivada no hace nada
        //y devuelve 200 igual
        Property property = jpaPropertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException(id));

        if (!agencyScopeService.canAccess(property.getAgencyId())) {
            throw new PropertyNotFoundException(id);
        }

        property.setActive(false);

        //Flush para que updatedAt salga actualizado en la respuesta
        jpaPropertyRepository.saveAndFlush(property);
        return propertyMapper.toResponse(property);
    }
}
