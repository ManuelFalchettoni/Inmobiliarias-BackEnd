package com.manuel.zaguan_inmobiliarias.exception.agency;

public class NotAgencyOwnerException extends RuntimeException {
    public NotAgencyOwnerException(String message) {
        super(message);
    }
}
