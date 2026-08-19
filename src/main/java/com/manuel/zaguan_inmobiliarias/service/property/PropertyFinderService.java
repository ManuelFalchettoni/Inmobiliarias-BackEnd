package com.manuel.zaguan_inmobiliarias.service.property;

import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertyResponse;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.PropertyMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PropertyFinderService {

    private final JpaPropertyRepository jpaPropertyRepository;
    private final PropertyMapper propertyMapper;

    public PropertyFinderService (JpaPropertyRepository jpaPropertyRepository, PropertyMapper propertyMapper){
        this.jpaPropertyRepository = jpaPropertyRepository;
        this.propertyMapper = propertyMapper;
    }

    public PropertyResponse findById(Long id) {
        return jpaPropertyRepository.findByIdAndActiveTrue(id)
                .map(propertyMapper::toResponse)
                .orElseThrow(() -> new PropertyNotFoundException(id));
    }

    public Page<PropertyResponse> findAll(Pageable pageable) {
        return jpaPropertyRepository.findAllByActiveTrue(pageable)
                .map(propertyMapper::toResponse);
    }

    public Page<PropertyResponse> findByAgency(Long idAgency, Pageable pageable) {
        return jpaPropertyRepository.findAllByIdAgencyAndActiveTrue(idAgency, pageable)
                .map(propertyMapper::toResponse);
    }
}
