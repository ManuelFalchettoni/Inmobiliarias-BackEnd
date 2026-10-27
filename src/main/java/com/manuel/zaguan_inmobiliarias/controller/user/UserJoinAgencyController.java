package com.manuel.zaguan_inmobiliarias.controller.user;

import com.manuel.zaguan_inmobiliarias.dto.request.user.UserJoinAgencyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.service.user.UserJoinAgencyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserJoinAgencyController {
    private final UserJoinAgencyService userJoinAgencyService;

    @PostMapping("/join-agency")
    public ResponseEntity<UserResponse> join(@Valid @RequestBody UserJoinAgencyRequest request){
        return ResponseEntity.ok(userJoinAgencyService.join(request));
    }
}
