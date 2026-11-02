package com.manuel.zaguan_inmobiliarias.exception.property;

//Culpa del cliente: un rango con el minimo mayor al maximo, o precio sin moneda
public class InvalidSearchFilterException extends RuntimeException {

    public InvalidSearchFilterException(String message){
        super(message);
    }
}
