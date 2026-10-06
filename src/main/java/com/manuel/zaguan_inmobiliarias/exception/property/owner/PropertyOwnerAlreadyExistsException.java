package com.manuel.zaguan_inmobiliarias.exception.property.owner;

public class PropertyOwnerAlreadyExistsException extends RuntimeException {
    public PropertyOwnerAlreadyExistsException(Long propertyId, Long peopleId) {
        super("Property owner of property with id: " + propertyId + " and people with id: " + peopleId + " already exists.");
    }
}
