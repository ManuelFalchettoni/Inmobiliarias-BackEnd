package com.manuel.zaguan_inmobiliarias.exception.crm;

public class CrmPropertyNotFoundException extends RuntimeException {
    public CrmPropertyNotFoundException(Long id) {
        super("Crm property with id: " + id + " not found.");
    }
}
