package com.manuel.zaguan_inmobiliarias.controller.auth.password;

import com.manuel.zaguan_inmobiliarias.dto.request.auth.password.PasswordForgotRequest;
import com.manuel.zaguan_inmobiliarias.dto.request.auth.password.PasswordResetRequest;
import com.manuel.zaguan_inmobiliarias.service.auth.password.PasswordForgotService;
import com.manuel.zaguan_inmobiliarias.service.auth.password.PasswordResetService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Recuperar la contraseña sin estar logueado. Cuelga de /api/auth, que no pide sesion
@RestController
@RequestMapping("/api/auth/password")
@AllArgsConstructor
public class AuthPasswordResetController {
    private final PasswordForgotService passwordForgotService;
    private final PasswordResetService passwordResetService;

    //Siempre 204, exista el email o no
    @PostMapping("/forgot")
    public ResponseEntity<Void> forgot(@Valid @RequestBody PasswordForgotRequest request) {
        passwordForgotService.requestReset(request.email());
        return ResponseEntity.noContent().build();
    }

    //204 si se cambio; 400 si el link no sirve (InvalidPasswordResetTokenException)
    @PostMapping("/reset")
    public ResponseEntity<Void> reset(@Valid @RequestBody PasswordResetRequest request) {
        passwordResetService.resetPassword(request);
        return ResponseEntity.noContent().build();
    }
}
