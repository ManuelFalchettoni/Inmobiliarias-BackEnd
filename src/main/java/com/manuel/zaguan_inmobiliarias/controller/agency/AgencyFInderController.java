package com.manuel.zaguan_inmobiliarias.controller.agency;

import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.service.agency.AgencyFinderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/agencies")
//El listado esta en AgenciesGetController, con el filtro active y @PageableDefault
public class AgencyFInderController {
    private final AgencyFinderService agencyFinderService;

    @GetMapping("/{id}")
    public ResponseEntity<AgencyResponse> get(@PathVariable Long id){
        AgencyResponse agencyResponse = agencyFinderService.findById(id);

        return ResponseEntity.ok(agencyResponse);
    }
}
