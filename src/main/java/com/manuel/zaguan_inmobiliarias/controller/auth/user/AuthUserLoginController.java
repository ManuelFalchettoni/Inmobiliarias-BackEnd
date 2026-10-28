package com.manuel.zaguan_inmobiliarias.controller.auth.user;

import com.manuel.zaguan_inmobiliarias.dto.request.auth.user.AuthUserLoginRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.mapper.user.UserMapper;
import com.manuel.zaguan_inmobiliarias.service.auth.user.AuthUserLoginService;
import com.manuel.zaguan_inmobiliarias.service.jwt.JwtService;
import com.manuel.zaguan_inmobiliarias.service.user.UserFinderService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
    private final UserFinderService userFinderService;
    private final UserMapper userMapper;

    @PostMapping()
    public ResponseEntity<UserResponse> login(@Valid @RequestBody AuthUserLoginRequest loginRequest,
                                              HttpServletResponse response) {
        String token = authUserLoginService.login(loginRequest);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        if(Boolean.TRUE.equals(loginRequest.getRemember())) cookie.setMaxAge(86400);
        response.addCookie(cookie);

        //El usuario completo y no solo lo que trae el token: asi el front recibe agencyId y el
        //logo de la inmobiliaria (agencyIconUrl) apenas se loguea
        Long userId = jwtService.getUserIdFromToken(token);
        UserResponse userResponse = userMapper.toResponse(userFinderService.find(userId));
        return ResponseEntity.ok(userResponse);
    }

}

