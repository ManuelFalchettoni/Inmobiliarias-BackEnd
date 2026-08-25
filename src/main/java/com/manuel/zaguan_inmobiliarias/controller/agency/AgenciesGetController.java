package com.manuel.zaguan_inmobiliarias.controller.agency;

import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.service.agency.AgenciesSearcherService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping
    public ResponseEntity<Page<AgencyResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<AgencyResponse> agencyResponses = agenciesSearcherService.findAll(pageable);

        return ResponseEntity.ok(agencyResponses);
    }
}
