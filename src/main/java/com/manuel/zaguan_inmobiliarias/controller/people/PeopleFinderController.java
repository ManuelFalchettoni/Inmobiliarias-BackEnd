package com.manuel.zaguan_inmobiliarias.controller.people;

import com.manuel.zaguan_inmobiliarias.dto.response.people.PeopleResponse;
import com.manuel.zaguan_inmobiliarias.service.people.PeopleFinderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/people")
public class PeopleFinderController {
    private final PeopleFinderService peopleFinderService;

    @GetMapping("/{id}")
    public ResponseEntity<PeopleResponse> findById(@PathVariable Long id){
        PeopleResponse peopleResponse = peopleFinderService.findById(id);

        return ResponseEntity.ok(peopleResponse);
    }

    @GetMapping
    public ResponseEntity<Page<PeopleResponse>> findAll(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);

        Page<PeopleResponse> peoplesResponse = peopleFinderService.findAll(pageable);

        return ResponseEntity.ok(peoplesResponse);
    }
}
