package com.manuel.zaguan_inmobiliarias.service.property.price;

import com.manuel.zaguan_inmobiliarias.dto.response.property.price.PropertyPriceResponse;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.price.PropertyPriceNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.price.PropertyPriceMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.price.JpaPropertyPriceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PropertyPriceFinderService {

    private final JpaPropertyPriceRepository jpaPropertyPriceRepository;
    private final JpaPropertyRepository jpaPropertyRepository;
    private final PropertyPriceMapper propertyPriceMapper;

    public PropertyPriceFinderService(JpaPropertyPriceRepository jpaPropertyPriceRepository,
                                      JpaPropertyRepository jpaPropertyRepository,
                                      PropertyPriceMapper propertyPriceMapper) {
        this.jpaPropertyPriceRepository = jpaPropertyPriceRepository;
        this.jpaPropertyRepository = jpaPropertyRepository;
        this.propertyPriceMapper = propertyPriceMapper;
    }

    public List<PropertyPriceResponse> findByProperty(Long propertyId) {
        requireProperty(propertyId);

        return jpaPropertyPriceRepository.findByPropertyIdOrderByOperationTypeAsc(propertyId)
                .stream()
                .map(propertyPriceMapper::toResponse)
                .toList();
    }

    public PropertyPriceResponse findById(Long propertyId, Long priceId) {
        requireProperty(propertyId);

        return jpaPropertyPriceRepository.findByIdAndPropertyId(priceId, propertyId)
                .map(propertyPriceMapper::toResponse)
                .orElseThrow(() -> new PropertyPriceNotFoundException(priceId));
    }

    private void requireProperty(Long propertyId) {
        if (!jpaPropertyRepository.existsByIdAndActiveTrue(propertyId)) {
            throw new PropertyNotFoundException(propertyId);
        }
    }
}
