package com.manuel.zaguan_inmobiliarias.service.property.price;

import com.manuel.zaguan_inmobiliarias.dto.request.property.price.PropertyPriceRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.price.PropertyPriceResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.price.PropertyPrice;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.price.PropertyPriceAlreadyExistsException;
import com.manuel.zaguan_inmobiliarias.mapper.property.price.PropertyPriceMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.price.JpaPropertyPriceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertyPriceCreatorService {

    private final JpaPropertyPriceRepository jpaPropertyPriceRepository;
    private final JpaPropertyRepository jpaPropertyRepository;
    private final PropertyPriceMapper propertyPriceMapper;

    public PropertyPriceCreatorService(JpaPropertyPriceRepository jpaPropertyPriceRepository,
                                       JpaPropertyRepository jpaPropertyRepository,
                                       PropertyPriceMapper propertyPriceMapper) {
        this.jpaPropertyPriceRepository = jpaPropertyPriceRepository;
        this.jpaPropertyRepository = jpaPropertyRepository;
        this.propertyPriceMapper = propertyPriceMapper;
    }

    @Transactional
    public PropertyPriceResponse create(Long propertyId, PropertyPriceRequest request) {
        if (!jpaPropertyRepository.existsByIdAndActiveTrue(propertyId)) {
            throw new PropertyNotFoundException(propertyId);
        }
        if (jpaPropertyPriceRepository.existsByPropertyIdAndOperationType(propertyId, request.getOperationType())) {
            throw new PropertyPriceAlreadyExistsException(propertyId, request.getOperationType());
        }

        PropertyPrice price = propertyPriceMapper.toEntity(request);
        //getReferenceById no consulta la base: ya sabemos que la propiedad existe
        price.setProperty(jpaPropertyRepository.getReferenceById(propertyId));

        return propertyPriceMapper.toResponse(jpaPropertyPriceRepository.save(price));
    }
}
