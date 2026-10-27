package com.manuel.zaguan_inmobiliarias.service.property;

import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertyDeleteService {
    private final JpaPropertyRepository jpaPropertyRepository;
    private final AgencyScopeService agencyScopeService;

    public PropertyDeleteService (JpaPropertyRepository jpaPropertyRepository, AgencyScopeService agencyScopeService){
        this.jpaPropertyRepository = jpaPropertyRepository;
        this.agencyScopeService = agencyScopeService;
    }

    @Transactional //Necesario porque no hay un metodo save para guardar el cambio
    public void delete(Long id){
        Property property = jpaPropertyRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new PropertyNotFoundException(id));

        if (!agencyScopeService.canAccess(property.getAgencyId())) {
            throw new PropertyNotFoundException(id);
        }

        property.setActive(false);
    }
}
