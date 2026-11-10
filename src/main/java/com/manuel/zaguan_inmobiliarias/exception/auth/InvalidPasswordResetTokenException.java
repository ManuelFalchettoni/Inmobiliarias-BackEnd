package com.manuel.zaguan_inmobiliarias.exception.auth;

//El link de recuperacion no existe, ya se uso, vencio o es de un usuario dado de baja.
//Un solo mensaje para todos los casos: el front solo puede ofrecer pedir otro
public class InvalidPasswordResetTokenException extends RuntimeException {
    public InvalidPasswordResetTokenException() {
        super("The password reset link is invalid or has expired");
    }
}
