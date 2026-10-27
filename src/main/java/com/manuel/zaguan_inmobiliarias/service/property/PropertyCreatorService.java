package com.manuel.zaguan_inmobiliarias.service.property;

import com.manuel.zaguan_inmobiliarias.dto.request.property.PropertyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertyResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.exception.agency.AgencyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.PropertyMapper;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertyCreatorService {
    private final JpaPropertyRepository jpaPropertyRepository;
    private final JpaAgencyRepository jpaAgencyRepository;
    private final PropertyMapper propertyMapper;
    private final AgencyScopeService agencyScopeService;

    public PropertyCreatorService (JpaPropertyRepository jpaPropertyRepository, PropertyMapper propertyMapper,
                                   JpaAgencyRepository jpaAgencyRepository, AgencyScopeService agencyScopeService){
        this.jpaPropertyRepository = jpaPropertyRepository;
        this.jpaAgencyRepository = jpaAgencyRepository;
        this.propertyMapper = propertyMapper;
        this.agencyScopeService = agencyScopeService;
    }

    @Transactional
    public PropertyResponse create(PropertyRequest request){
        //Una inmobiliaria carga siempre a su nombre, mande el agencyId que mande
        request.setAgencyId(agencyScopeService.resolveAgencyId(request.getAgencyId()));

        //existsByIdAndActiveTrue: no se cargan propiedades a una inmobiliaria dada de baja
        if (jpaAgencyRepository.existsByIdAndActiveTrue(request.getAgencyId())){
            Property property = jpaPropertyRepository.save(propertyMapper.toEntity(request));
            return propertyMapper.toResponse(property);
        }
        throw new AgencyNotFoundException(request.getAgencyId());

    }
}
