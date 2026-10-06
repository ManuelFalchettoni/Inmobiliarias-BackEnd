package com.manuel.zaguan_inmobiliarias.controller.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.CrmAlertRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmAlertResponse;
import com.manuel.zaguan_inmobiliarias.service.crm.CrmAlertCreatorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crm_properties/{crmPropertyId}/alerts")
@AllArgsConstructor
public class CrmAlertCreatorController {
    private final CrmAlertCreatorService crmAlertCreatorService;

    @PostMapping
    public ResponseEntity<CrmAlertResponse> create(@PathVariable Long crmPropertyId,
                                                   @Valid @RequestBody CrmAlertRequest request){
        CrmAlertResponse response = crmAlertCreatorService.create(crmPropertyId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
