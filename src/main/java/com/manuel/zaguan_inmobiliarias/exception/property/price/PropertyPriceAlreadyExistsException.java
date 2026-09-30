package com.manuel.zaguan_inmobiliarias.exception.property.price;

import com.manuel.zaguan_inmobiliarias.enums.property.OperationType;

//La propiedad ya tiene un precio para esa operacion: se edita ese, no se agrega otro
public class PropertyPriceAlreadyExistsException extends RuntimeException {

    public PropertyPriceAlreadyExistsException(Long propertyId, OperationType operationType) {
        super("Property " + propertyId + " already has a " + operationType + " price");
    }
}
