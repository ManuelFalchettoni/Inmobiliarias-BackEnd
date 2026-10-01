package com.manuel.zaguan_inmobiliarias.controller.property.contract;

import com.manuel.zaguan_inmobiliarias.dto.request.property.contract.PropertyContractRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.contract.PropertyContractResponse;
import com.manuel.zaguan_inmobiliarias.service.property.contract.PropertyContractCreatorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/property_contracts")
@AllArgsConstructor
public class PropertyContractCreatorController {
    private final PropertyContractCreatorService propertyContractCreatorService;

    @PostMapping
    public ResponseEntity<PropertyContractResponse> create (@Valid @RequestBody PropertyContractRequest propertyContractRequest){

        PropertyContractResponse propertyContractResponse = propertyContractCreatorService.create(propertyContractRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(propertyContractResponse);
    }
}
