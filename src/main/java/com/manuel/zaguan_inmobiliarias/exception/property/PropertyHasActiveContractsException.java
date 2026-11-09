package com.manuel.zaguan_inmobiliarias.exception.property;

//Se quiso archivar una propiedad que tiene contratos vigentes (ACTIVE). Primero hay que
//cancelarlos o finalizarlos
public class PropertyHasActiveContractsException extends RuntimeException {
    public PropertyHasActiveContractsException(Long propertyId) {
        super("Property with id: " + propertyId + " has active contracts and cannot be archived.");
    }
}
