package com.manuel.zaguan_inmobiliarias.exception.auth;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
