package com.manuel.zaguan_inmobiliarias.controller.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.CrmAlertRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmAlertResponse;
import com.manuel.zaguan_inmobiliarias.service.crm.CrmAlertUpdaterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crm_properties/{crmPropertyId}/alerts")
@AllArgsConstructor
public class CrmAlertUpdaterController {
    private final CrmAlertUpdaterService crmAlertUpdaterService;

    @PutMapping("/{alertId}")
    public ResponseEntity<CrmAlertResponse> update(@PathVariable Long crmPropertyId,
                                                   @PathVariable Long alertId,
                                                   @Valid @RequestBody CrmAlertRequest request){
        return ResponseEntity.ok(crmAlertUpdaterService.update(crmPropertyId, alertId, request));
    }
}
