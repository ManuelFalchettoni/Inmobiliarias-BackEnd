package com.manuel.zaguan_inmobiliarias.exception.property;

import lombok.Getter;

@Getter
public class PropertyNotFoundException extends RuntimeException {

    private final Long propertyId;

    public PropertyNotFoundException(Long propertyId) {
        super("Property not found with id: " + propertyId);
        this.propertyId = propertyId;
    }

    public PropertyNotFoundException(String message) {
        super(message);
        this.propertyId = null;
    }
}
