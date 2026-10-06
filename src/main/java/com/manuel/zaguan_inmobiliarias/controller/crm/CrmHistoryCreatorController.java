package com.manuel.zaguan_inmobiliarias.controller.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.CrmHistoryRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmHistoryResponse;
import com.manuel.zaguan_inmobiliarias.service.crm.CrmHistoryCreatorService;
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
@RequestMapping("/api/crm_properties/{crmPropertyId}/history")
@AllArgsConstructor
public class CrmHistoryCreatorController {
    private final CrmHistoryCreatorService crmHistoryCreatorService;

    @PostMapping
    public ResponseEntity<CrmHistoryResponse> create(@PathVariable Long crmPropertyId,
                                                     @Valid @RequestBody CrmHistoryRequest request){
        CrmHistoryResponse response = crmHistoryCreatorService.create(crmPropertyId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
