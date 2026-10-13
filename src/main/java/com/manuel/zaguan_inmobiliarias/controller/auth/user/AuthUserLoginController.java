package com.manuel.zaguan_inmobiliarias.controller.auth.user;

import com.manuel.zaguan_inmobiliarias.dto.request.auth.user.AuthUserLoginRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.service.auth.user.AuthUserLoginService;
import com.manuel.zaguan_inmobiliarias.service.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/login")
@AllArgsConstructor
public class AuthUserLoginController {
    private final AuthUserLoginService authUserLoginService;
    private final JwtService jwtService;

    @PostMapping()
    public ResponseEntity<UserResponse> login(@RequestBody AuthUserLoginRequest loginRequest,
                                              HttpServletResponse response) {
        String token = authUserLoginService.login(loginRequest);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        if(loginRequest.isRemember()) cookie.setMaxAge(86400);
        response.addCookie(cookie);

        UserResponse userResponse = jwtService.getUserResponse(token);
        return ResponseEntity.ok(userResponse);
    }

}

