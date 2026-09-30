package com.manuel.zaguan_inmobiliarias.service.property.price;

import com.manuel.zaguan_inmobiliarias.entity.property.price.PropertyPrice;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.price.PropertyPriceNotFoundException;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.price.JpaPropertyPriceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//Borrado fisico, como las fotos: un precio viejo no se restaura, se carga de nuevo
@Service
public class PropertyPriceDeleteService {

    private final JpaPropertyPriceRepository jpaPropertyPriceRepository;
    private final JpaPropertyRepository jpaPropertyRepository;

    public PropertyPriceDeleteService(JpaPropertyPriceRepository jpaPropertyPriceRepository,
                                      JpaPropertyRepository jpaPropertyRepository) {
        this.jpaPropertyPriceRepository = jpaPropertyPriceRepository;
        this.jpaPropertyRepository = jpaPropertyRepository;
    }

    @Transactional
    public void delete(Long propertyId, Long priceId) {
        if (!jpaPropertyRepository.existsByIdAndActiveTrue(propertyId)) {
            throw new PropertyNotFoundException(propertyId);
        }

        PropertyPrice price = jpaPropertyPriceRepository.findByIdAndPropertyId(priceId, propertyId)
                .orElseThrow(() -> new PropertyPriceNotFoundException(priceId));

        jpaPropertyPriceRepository.delete(price);
    }
}
