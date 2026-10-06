package com.manuel.zaguan_inmobiliarias.service.property;

import com.manuel.zaguan_inmobiliarias.dto.request.property.PropertyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertyResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyAgencyMismatchException;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.PropertyMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class PropertyUpdaterService {
    private final JpaPropertyRepository jpaPropertyRepository;
    private final PropertyMapper propertyMapper;

    public PropertyUpdaterService (JpaPropertyRepository jpaPropertyRepository, PropertyMapper propertyMapper){
        this.jpaPropertyRepository = jpaPropertyRepository;
        this.propertyMapper = propertyMapper;
    }

    @Transactional
    public PropertyResponse update(Long id, PropertyRequest request){
        Property property = jpaPropertyRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new PropertyNotFoundException(id));

        //El PropertyMapper no copia agencyId, asi que antes el campo se descartaba en silencio:
        //el cliente mandaba otra inmobiliaria, recibia 200 y la propiedad no se movia.
        //Objects.equals y no equals: la columna agency_id acepta null
        if (!Objects.equals(property.getAgencyId(), request.getAgencyId())) {
            throw new PropertyAgencyMismatchException(id, property.getAgencyId(), request.getAgencyId());
        }

        propertyMapper.updateEntity(request, property);

        jpaPropertyRepository.saveAndFlush(property);
        return propertyMapper.toResponse(property);
    }
}
