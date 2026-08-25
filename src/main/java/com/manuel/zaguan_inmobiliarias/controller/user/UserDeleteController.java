package com.manuel.zaguan_inmobiliarias.controller.user;

import com.manuel.zaguan_inmobiliarias.service.user.UserDeleterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserDeleteController {
    private final UserDeleterService userDeleterService;

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        userDeleterService.deleter(id);
        return ResponseEntity.noContent().build();
    }
}
