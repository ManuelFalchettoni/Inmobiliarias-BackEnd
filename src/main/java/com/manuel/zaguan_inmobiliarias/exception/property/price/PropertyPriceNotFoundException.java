package com.manuel.zaguan_inmobiliarias.exception.property.price;

import lombok.Getter;

@Getter
public class PropertyPriceNotFoundException extends RuntimeException {

    private final Long propertyPriceId;

    public PropertyPriceNotFoundException(Long propertyPriceId) {
        super("Property price not found with id: " + propertyPriceId);
        this.propertyPriceId = propertyPriceId;
    }
}
