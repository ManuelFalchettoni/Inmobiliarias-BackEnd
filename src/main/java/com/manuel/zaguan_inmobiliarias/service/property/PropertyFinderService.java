package com.manuel.zaguan_inmobiliarias.service.property;

import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertyResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyCondition;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyOccupancy;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyType;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.PropertyMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PropertyFinderService {

    private final JpaPropertyRepository jpaPropertyRepository;
    private final PropertyMapper propertyMapper;
    private final AgencyScopeService agencyScopeService;

    public PropertyFinderService (JpaPropertyRepository jpaPropertyRepository, PropertyMapper propertyMapper,
                                  AgencyScopeService agencyScopeService){
        this.jpaPropertyRepository = jpaPropertyRepository;
        this.propertyMapper = propertyMapper;
        this.agencyScopeService = agencyScopeService;
    }

    //Una de otra inmobiliaria responde 404, igual que una que no existe
    public PropertyResponse findById(Long id) {
        Property property = jpaPropertyRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new PropertyNotFoundException(id));

        if (!agencyScopeService.canAccess(property.getAgencyId())) {
            throw new PropertyNotFoundException(id);
        }
        return propertyMapper.toResponse(property);
    }

    //agencyId lo respeta solo el admin; en null lista todas
    public Page<PropertyResponse> findAll(Long agencyId, Boolean active, Pageable pageable) {
        Long scopedAgencyId = agencyScopeService.resolveAgencyId(agencyId);

        Page<Property> properties = (scopedAgencyId == null)
                ? jpaPropertyRepository.findAllByActive(active, pageable)
                : jpaPropertyRepository.findAllByAgencyIdAndActive(scopedAgencyId, active, pageable);
        return properties.map(propertyMapper::toResponse);
    }

    public Page<PropertyResponse> search(Long agencyId, PropertyType type, String province, String county,
                                         String city, Integer rooms, PropertyCondition condition,
                                         PropertyOccupancy occupancy, Integer minSize, Integer maxSize,
                                         Integer minYear, Integer maxYear, Pageable pageable) {
        Long scopedAgencyId = agencyScopeService.resolveAgencyId(agencyId);

        return jpaPropertyRepository.search(scopedAgencyId, type, province, county, city, rooms, condition,
                        occupancy, minSize, maxSize, minYear, maxYear, pageable)
                .map(propertyMapper::toResponse);
    }
}
