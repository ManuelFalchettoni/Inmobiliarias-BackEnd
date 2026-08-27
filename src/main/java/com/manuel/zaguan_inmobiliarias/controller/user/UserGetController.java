package com.manuel.zaguan_inmobiliarias.controller.user;

import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.service.user.UserFinderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserGetController {
    private final UserFinderService userFinderService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable Long id){
        UserResponse userResponse = userFinderService.findById(id);
        return ResponseEntity.ok(userResponse);
    }
}
