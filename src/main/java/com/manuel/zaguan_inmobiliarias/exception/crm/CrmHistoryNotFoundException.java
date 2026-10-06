package com.manuel.zaguan_inmobiliarias.exception.crm;

public class CrmHistoryNotFoundException extends RuntimeException {
    public CrmHistoryNotFoundException(Long id) {
        super("Crm history with id: " + id + " not found.");
    }
}
