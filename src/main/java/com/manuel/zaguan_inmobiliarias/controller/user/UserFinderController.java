package com.manuel.zaguan_inmobiliarias.controller.user;

import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.service.user.UserFinderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserFinderController {
    private final UserFinderService userFinderService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable Long id){
        UserResponse userResponse = userFinderService.findById(id);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "5") int size){
        int cappedSize = Math.min(size, 50);
        Pageable pageable = PageRequest.of(page, cappedSize);
        Page<UserResponse> usersResponse = userFinderService.findAll(pageable);
        return ResponseEntity.ok(usersResponse);
    }
}
