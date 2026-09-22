package com.manuel.zaguan_inmobiliarias.exception;

//La contraseña actual que manda el cliente no coincide con la guardada. Vive en la raiz de
//exception y no en un paquete por entidad porque la usan usuarios e inmobiliarias igual
public class InvalidCurrentPasswordException extends RuntimeException {
    public InvalidCurrentPasswordException() {
        super("Current password does not match");
    }
}
