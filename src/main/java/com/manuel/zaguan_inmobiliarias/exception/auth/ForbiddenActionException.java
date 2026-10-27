package com.manuel.zaguan_inmobiliarias.exception.auth;

//El usuario esta logueado pero no puede hacer esto, por ejemplo una inmobiliaria creando un ADMIN
public class ForbiddenActionException extends RuntimeException {
    public ForbiddenActionException(String message) {
        super(message);
    }
}
