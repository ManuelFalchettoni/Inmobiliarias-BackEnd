package com.manuel.zaguan_inmobiliarias.exception.property.photo;

public class PhotoStorageException extends RuntimeException {

    public PhotoStorageException(String message){
        super(message);
    }

    public PhotoStorageException(String message, Throwable cause){
        super(message, cause);
    }
}
