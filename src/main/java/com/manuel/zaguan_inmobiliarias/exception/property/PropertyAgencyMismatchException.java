package com.manuel.zaguan_inmobiliarias.exception.property;

import lombok.Getter;

//El PUT trae un agencyId distinto al que la propiedad ya tiene. La propiedad no cambia
//de inmobiliaria desde el update, asi que se avisa en vez de ignorar el campo
@Getter
public class PropertyAgencyMismatchException extends RuntimeException {

    private final Long propertyId;

    public PropertyAgencyMismatchException(Long propertyId, Long currentAgencyId, Long requestedAgencyId) {
        super("Property " + propertyId + " belongs to agency " + currentAgencyId
                + " and cannot be moved to agency " + requestedAgencyId);
        this.propertyId = propertyId;
    }
}
