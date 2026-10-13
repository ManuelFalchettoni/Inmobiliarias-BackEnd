package com.manuel.zaguan_inmobiliarias.exception.auth;

//Email o contraseña incorrectos en el login. Es una sola excepcion para los dos casos a
//proposito: si el mensaje dijera cual fallo, se podria averiguar que emails estan registrados
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}
