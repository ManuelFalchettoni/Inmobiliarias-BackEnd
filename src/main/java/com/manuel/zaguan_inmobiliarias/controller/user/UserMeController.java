package com.manuel.zaguan_inmobiliarias.controller.user;

import com.manuel.zaguan_inmobiliarias.dto.request.user.UserPasswordRequest;
import com.manuel.zaguan_inmobiliarias.dto.request.user.UserUpdateRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.service.user.UserFinderService;
import com.manuel.zaguan_inmobiliarias.service.user.UserPasswordUpdaterService;
import com.manuel.zaguan_inmobiliarias.service.user.UserUpdaterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//La cuenta del que esta logueado: el usuario sale del token, no de la URL.
//Lo puede usar cualquiera logueado, tambien un USER que todavia no es de ninguna inmobiliaria
@RestController
@RequestMapping("/api/users/me")
@AllArgsConstructor
public class UserMeController {
    private final UserFinderService userFinderService;
    private final UserUpdaterService userUpdaterService;
    private final UserPasswordUpdaterService userPasswordUpdaterService;

    @GetMapping
    public ResponseEntity<UserResponse> get(){
        return ResponseEntity.ok(userFinderService.findMe());
    }

    @PutMapping
    public ResponseEntity<UserResponse> put(@Valid @RequestBody UserUpdateRequest userUpdateRequest){
        return ResponseEntity.ok(userUpdaterService.updateMe(userUpdateRequest));
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody UserPasswordRequest userPasswordRequest){
        userPasswordUpdaterService.updateMyPassword(userPasswordRequest);
        return ResponseEntity.noContent().build();
    }
}
