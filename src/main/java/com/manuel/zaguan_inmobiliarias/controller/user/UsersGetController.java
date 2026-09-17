package com.manuel.zaguan_inmobiliarias.controller.user;

import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.service.user.UsersSearcherService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UsersGetController {

    private final UsersSearcherService usersSearcherService;

    //Pageable en vez de armar el PageRequest a mano: asi respeta el tope de
    //spring.data.web.pageable.max-page-size y un size invalido no rompe.
    //active por defecto en true: el que no lo manda ve solo los vigentes
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(
            @RequestParam(defaultValue = "true") Boolean active,
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){

        Page<UserResponse> usersResponse = usersSearcherService.findAll(active, pageable);
        return ResponseEntity.ok(usersResponse);
    }
}
