package com.manuel.zaguan_inmobiliarias.exception.agency;

public class AgencyAlreadyDeletedException extends RuntimeException {
    public AgencyAlreadyDeletedException(String message) {
        super(message);
    }
}
