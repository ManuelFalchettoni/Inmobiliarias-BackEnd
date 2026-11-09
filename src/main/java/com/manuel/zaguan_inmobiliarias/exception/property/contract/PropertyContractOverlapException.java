package com.manuel.zaguan_inmobiliarias.exception.property.contract;

public class PropertyContractOverlapException extends RuntimeException {
    public PropertyContractOverlapException(Long propertyId) {
        super("Property with id: " + propertyId + " already has an active contract of the same type in that period.");
    }
}
