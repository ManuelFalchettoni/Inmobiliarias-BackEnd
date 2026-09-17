package com.manuel.zaguan_inmobiliarias.controller.agency;

import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.service.agency.AgenciesSearcherService;
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
@RequestMapping("/api/agencies")
@AllArgsConstructor
public class AgenciesGetController {
    private final AgenciesSearcherService agenciesSearcherService;

    //Pageable en vez de armar el PageRequest a mano: asi respeta el tope de
    //spring.data.web.pageable.max-page-size y un size invalido no rompe.
    //active por defecto en true: el que no lo manda ve solo las vigentes
    @GetMapping
    public ResponseEntity<Page<AgencyResponse>> getAll(
            @RequestParam(defaultValue = "true") Boolean active,
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){

        Page<AgencyResponse> agencyResponses = agenciesSearcherService.findAll(active, pageable);

        return ResponseEntity.ok(agencyResponses);
    }
}
