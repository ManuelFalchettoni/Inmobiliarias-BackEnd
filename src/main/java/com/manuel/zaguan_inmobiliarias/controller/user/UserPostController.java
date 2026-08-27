package com.manuel.zaguan_inmobiliarias.controller.user;

import com.manuel.zaguan_inmobiliarias.dto.request.auth.user.AuthUserRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.service.auth.AuthUserRegisterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserPostController {
    private final AuthUserRegisterService authUserRegisterService;

    @PostMapping
    public ResponseEntity<UserResponse> create (@Valid @RequestBody AuthUserRequest userRequest){
        UserResponse userResponse = authUserRegisterService.userRegister(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

}
