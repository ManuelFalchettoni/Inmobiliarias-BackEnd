package com.manuel.zaguan_inmobiliarias.controller.agency;

import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.service.agency.AgencyFinderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/agencies")
public class AgencyFInderController {
    private final AgencyFinderService agencyFinderService;

    @GetMapping("/{id}")
    public ResponseEntity<AgencyResponse> get(@PathVariable Long id){
        AgencyResponse agencyResponse = agencyFinderService.findById(id);

        return ResponseEntity.ok(agencyResponse);
    }

    @GetMapping
    public ResponseEntity<Page<AgencyResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<AgencyResponse> agencyResponses = agencyFinderService.findAll(pageable);

        return ResponseEntity.ok(agencyResponses);
    }
}
