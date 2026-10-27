package com.manuel.zaguan_inmobiliarias.controller.auth.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Sin service: no hay nada que buscar ni guardar, solo borrar la cookie.
//La cookie es HttpOnly, asi que el front no la puede borrar: la tiene que pisar el backend.
//Se pisa con el mismo nombre y path que la del login y maxAge 0, que el navegador toma como "borrala"
@RestController
@RequestMapping("/api/auth/logout")
public class AuthUserLogoutController {

    @PostMapping
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }
}
