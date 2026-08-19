package com.manuel.zaguan_inmobiliarias.controller.user;

import com.manuel.zaguan_inmobiliarias.dto.request.user.UserRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.service.user.UserUpdaterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserPutController {
    private final UserUpdaterService userUpdaterService;

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> put (@PathVariable Long id,
                                             @Valid @RequestBody UserRequest userRequest){
        UserResponse userResponse = userUpdaterService.update(id, userRequest);
        return ResponseEntity.ok(userResponse);
    }

}
