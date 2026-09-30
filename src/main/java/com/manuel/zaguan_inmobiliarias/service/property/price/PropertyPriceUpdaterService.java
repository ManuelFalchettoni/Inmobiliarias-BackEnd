package com.manuel.zaguan_inmobiliarias.service.property.price;

import com.manuel.zaguan_inmobiliarias.dto.request.property.price.PropertyPriceRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.price.PropertyPriceResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.price.PropertyPrice;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.price.PropertyPriceAlreadyExistsException;
import com.manuel.zaguan_inmobiliarias.exception.property.price.PropertyPriceNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.price.PropertyPriceMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.price.JpaPropertyPriceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertyPriceUpdaterService {

    private final JpaPropertyPriceRepository jpaPropertyPriceRepository;
    private final JpaPropertyRepository jpaPropertyRepository;
    private final PropertyPriceMapper propertyPriceMapper;

    public PropertyPriceUpdaterService(JpaPropertyPriceRepository jpaPropertyPriceRepository,
                                       JpaPropertyRepository jpaPropertyRepository,
                                       PropertyPriceMapper propertyPriceMapper) {
        this.jpaPropertyPriceRepository = jpaPropertyPriceRepository;
        this.jpaPropertyRepository = jpaPropertyRepository;
        this.propertyPriceMapper = propertyPriceMapper;
    }

    @Transactional
    public PropertyPriceResponse update(Long propertyId, Long priceId, PropertyPriceRequest request) {
        if (!jpaPropertyRepository.existsByIdAndActiveTrue(propertyId)) {
            throw new PropertyNotFoundException(propertyId);
        }

        //Por id y por property: un precio de otra propiedad no se edita desde esta URL
        PropertyPrice price = jpaPropertyPriceRepository.findByIdAndPropertyId(priceId, propertyId)
                .orElseThrow(() -> new PropertyPriceNotFoundException(priceId));

        //El control va antes de los set, como en UserUpdaterService: la consulta exists haria flush
        if (jpaPropertyPriceRepository.existsByPropertyIdAndOperationTypeAndIdNot(propertyId, request.getOperationType(), priceId)) {
            throw new PropertyPriceAlreadyExistsException(propertyId, request.getOperationType());
        }

        propertyPriceMapper.updateEntity(request, price);

        return propertyPriceMapper.toResponse(jpaPropertyPriceRepository.saveAndFlush(price));
    }
}
