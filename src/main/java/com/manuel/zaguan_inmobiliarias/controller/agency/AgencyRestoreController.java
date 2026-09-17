package com.manuel.zaguan_inmobiliarias.controller.agency;

import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.service.agency.AgencyRestoreService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agencies")
@AllArgsConstructor
public class AgencyRestoreController {
    private final AgencyRestoreService agencyRestoreService;

    @PatchMapping("/{id}/restore")
    public ResponseEntity<AgencyResponse> restore(@PathVariable Long id){
        return ResponseEntity.ok(agencyRestoreService.restore(id));
    }
}
