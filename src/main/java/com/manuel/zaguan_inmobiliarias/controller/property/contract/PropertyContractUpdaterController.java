package com.manuel.zaguan_inmobiliarias.controller.property.contract;

import com.manuel.zaguan_inmobiliarias.dto.request.property.contract.PropertyContractRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.contract.PropertyContractResponse;
import com.manuel.zaguan_inmobiliarias.service.property.contract.PropertyContractUpdaterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/property_contracts")
public class PropertyContractUpdaterController {
    private final PropertyContractUpdaterService propertyContractUpdaterService;

    @PutMapping("/{id}")
    public ResponseEntity<PropertyContractResponse> update(@PathVariable Long id, @Valid @RequestBody PropertyContractRequest propertyContractRequest){
        PropertyContractResponse propertyContractResponse = propertyContractUpdaterService.update(id, propertyContractRequest);

        return ResponseEntity.ok(propertyContractResponse);
    }
}
