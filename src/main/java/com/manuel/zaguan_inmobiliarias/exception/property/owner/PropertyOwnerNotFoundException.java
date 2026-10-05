package com.manuel.zaguan_inmobiliarias.exception.property.owner;

public class PropertyOwnerNotFoundException extends RuntimeException {
    public PropertyOwnerNotFoundException(Long id) {
        super("Property owner with id: " + id + " not found.");
    }
}
