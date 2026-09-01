package com.manuel.zaguan_inmobiliarias.exception.property;

import lombok.Getter;

//El PUT trae un idAgency distinto al que la propiedad ya tiene. La propiedad no cambia
//de inmobiliaria desde el update, asi que se avisa en vez de ignorar el campo
@Getter
public class PropertyAgencyMismatchException extends RuntimeException {

    private final Long propertyId;

    public PropertyAgencyMismatchException(Long propertyId, Long currentIdAgency, Long requestedIdAgency) {
        super("Property " + propertyId + " belongs to agency " + currentIdAgency
                + " and cannot be moved to agency " + requestedIdAgency);
        this.propertyId = propertyId;
    }
}
