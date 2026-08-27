package com.manuel.zaguan_inmobiliarias.controller.agency;

import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.service.agency.AgencyFinderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/agencies")
public class AgencyGetController {
    private final AgencyFinderService agencyFinderService;

    @GetMapping("/{id}")
    public ResponseEntity<AgencyResponse> get(@PathVariable Long id){
        AgencyResponse agencyResponse = agencyFinderService.findById(id);

        return ResponseEntity.ok(agencyResponse);
    }
}
