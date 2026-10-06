package com.manuel.zaguan_inmobiliarias.controller.crm;

import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmAlertResponse;
import com.manuel.zaguan_inmobiliarias.service.crm.CrmAlertFinderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/crm_properties/{crmPropertyId}/alerts")
@AllArgsConstructor
public class CrmAlertFinderController {
    private final CrmAlertFinderService crmAlertFinderService;

    @GetMapping
    public ResponseEntity<List<CrmAlertResponse>> findByCrmProperty(@PathVariable Long crmPropertyId){
        return ResponseEntity.ok(crmAlertFinderService.findByCrmProperty(crmPropertyId));
    }

    @GetMapping("/{alertId}")
    public ResponseEntity<CrmAlertResponse> findById(@PathVariable Long crmPropertyId,
                                                     @PathVariable Long alertId){
        return ResponseEntity.ok(crmAlertFinderService.findById(crmPropertyId, alertId));
    }
}
