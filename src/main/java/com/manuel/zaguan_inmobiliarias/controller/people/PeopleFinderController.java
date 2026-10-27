package com.manuel.zaguan_inmobiliarias.controller.people;

import com.manuel.zaguan_inmobiliarias.dto.response.people.PeopleResponse;
import com.manuel.zaguan_inmobiliarias.service.people.PeopleFinderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    //Todos los filtros son opcionales. agencyId lo respeta solo el admin: una inmobiliaria busca
    //siempre en su cartera. Ejemplo: /api/people/search?name=garcia
    @GetMapping("/search")
    public ResponseEntity<Page<PeopleResponse>> search(@RequestParam(required = false) Long agencyId,
                                                       @RequestParam(required = false) String name,
                                                       @RequestParam(required = false) String email,
                                                       @RequestParam(required = false) String phone,
                                                       @RequestParam(required = false) String address,
                                                       @RequestParam(required = false) String dni,
                                                       @RequestParam(required = false) String cuit,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));

        Page<PeopleResponse> peoplesResponse = peopleFinderService.search(agencyId, name, email, phone,
                address, dni, cuit, pageable);

        return ResponseEntity.ok(peoplesResponse);
    }
}
