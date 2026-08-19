package com.manuel.zaguan_inmobiliarias.exception.property;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
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
