package com.manuel.zaguan_inmobiliarias.controller.people;

import com.manuel.zaguan_inmobiliarias.dto.request.people.PeopleRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.people.PeopleResponse;
import com.manuel.zaguan_inmobiliarias.service.people.PeopleCreatorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/people")
@AllArgsConstructor
public class PeopleCreatorController {
    private final PeopleCreatorService peopleCreatorService;

    @PostMapping
    public ResponseEntity<PeopleResponse> create(@Valid @RequestBody PeopleRequest peopleRequest){
        PeopleResponse peopleResponse = peopleCreatorService.create(peopleRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(peopleResponse);
    }
}
