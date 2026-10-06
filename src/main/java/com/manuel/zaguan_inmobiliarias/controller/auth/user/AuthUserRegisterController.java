package com.manuel.zaguan_inmobiliarias.controller.auth.user;

import com.manuel.zaguan_inmobiliarias.dto.request.auth.user.AuthUserRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.service.auth.user.AuthUserRegisterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
//Va aparte de /api/users: POST /api/users es el alta con inmobiliaria y rol (UserCreatorService)
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthUserRegisterController {

    private final AuthUserRegisterService authUserRegisterService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> create (@Valid @RequestBody AuthUserRequest userRequest){
        UserResponse userResponse = authUserRegisterService.userRegister(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
}
