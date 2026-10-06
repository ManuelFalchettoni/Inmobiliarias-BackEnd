package com.manuel.zaguan_inmobiliarias.exception.property.contract;

public class PropertyContractNotFoundException extends RuntimeException {
    public PropertyContractNotFoundException(Long id) {
        super("Property contract with id: " + id + " not found.");
    }
}
