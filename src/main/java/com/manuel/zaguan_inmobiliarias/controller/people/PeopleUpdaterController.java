package com.manuel.zaguan_inmobiliarias.controller.people;

import com.manuel.zaguan_inmobiliarias.dto.request.people.PeopleRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.people.PeopleResponse;
import com.manuel.zaguan_inmobiliarias.service.people.PeopleUpdaterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/people")
public class PeopleUpdaterController {

    private final PeopleUpdaterService peopleUpdaterService;

    @PutMapping("/{id}")
    public ResponseEntity<PeopleResponse> update(@PathVariable Long id, @Valid @RequestBody PeopleRequest peopleRequest){
        PeopleResponse peopleResponse = peopleUpdaterService.updater(id,peopleRequest);

        return ResponseEntity.ok(peopleResponse);
    }
}
