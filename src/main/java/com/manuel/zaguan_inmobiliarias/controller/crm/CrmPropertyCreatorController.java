package com.manuel.zaguan_inmobiliarias.controller.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.CrmPropertyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmPropertyResponse;
import com.manuel.zaguan_inmobiliarias.service.crm.CrmPropertyCreatorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crm_properties")
@AllArgsConstructor
public class CrmPropertyCreatorController {
    private final CrmPropertyCreatorService crmPropertyCreatorService;

    @PostMapping
    public ResponseEntity<CrmPropertyResponse> create(@Valid @RequestBody CrmPropertyRequest request){
        CrmPropertyResponse response = crmPropertyCreatorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
