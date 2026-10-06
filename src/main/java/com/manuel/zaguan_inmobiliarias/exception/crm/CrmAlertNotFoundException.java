package com.manuel.zaguan_inmobiliarias.exception.crm;

public class CrmAlertNotFoundException extends RuntimeException {
    public CrmAlertNotFoundException(Long id) {
        super("Crm alert with id: " + id + " not found.");
    }
}
