package com.manuel.zaguan_inmobiliarias.exception.property.photo;

//Culpa del cliente: archivo vacio, formato no permitido, request sin archivos
public class InvalidPhotoException extends RuntimeException {

    public InvalidPhotoException(String message){
        super(message);
    }
}
