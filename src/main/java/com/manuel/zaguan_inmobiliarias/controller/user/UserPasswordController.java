package com.manuel.zaguan_inmobiliarias.controller.user;

import com.manuel.zaguan_inmobiliarias.dto.request.user.UserPasswordRequest;
import com.manuel.zaguan_inmobiliarias.service.user.UserPasswordUpdaterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserPasswordController {
    private final UserPasswordUpdaterService userPasswordUpdaterService;

    //La contraseña sale del PUT: ahi se editan los datos, aca se cambia la contraseña
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id,
                                               @Valid @RequestBody UserPasswordRequest userPasswordRequest){
        userPasswordUpdaterService.updatePassword(id, userPasswordRequest);
        return ResponseEntity.noContent().build();
    }
}
