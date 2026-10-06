package com.manuel.zaguan_inmobiliarias.controller.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.CrmPropertyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmPropertyResponse;
import com.manuel.zaguan_inmobiliarias.service.crm.CrmPropertyUpdaterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crm_properties")
@AllArgsConstructor
public class CrmPropertyUpdaterController {
    private final CrmPropertyUpdaterService crmPropertyUpdaterService;

    @PutMapping("/{id}")
    public ResponseEntity<CrmPropertyResponse> update(@PathVariable Long id,
                                                      @Valid @RequestBody CrmPropertyRequest request){
        return ResponseEntity.ok(crmPropertyUpdaterService.update(id, request));
    }
}
