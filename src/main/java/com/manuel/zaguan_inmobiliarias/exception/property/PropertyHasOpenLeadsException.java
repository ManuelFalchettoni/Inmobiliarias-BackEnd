package com.manuel.zaguan_inmobiliarias.exception.property;

//Se quiso archivar una propiedad que tiene leads abiertos (ni CLOSED ni DISCARDED). Primero hay
//que cerrarlos o descartarlos
public class PropertyHasOpenLeadsException extends RuntimeException {
    public PropertyHasOpenLeadsException(Long propertyId) {
        super("Property with id: " + propertyId + " has open leads and cannot be archived.");
    }
}
