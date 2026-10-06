package com.manuel.zaguan_inmobiliarias.controller.property.contract;

import com.manuel.zaguan_inmobiliarias.dto.response.property.contract.PropertyContractResponse;
import com.manuel.zaguan_inmobiliarias.service.property.contract.PropertyContractFinderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/property_contracts")
public class PropertyContractFinderController {
    private final PropertyContractFinderService propertyContractFinderService;

    @GetMapping("/{id}")
    public ResponseEntity<PropertyContractResponse> findById(@PathVariable Long id){
        PropertyContractResponse propertyContractResponse = propertyContractFinderService.findById(id);

        return ResponseEntity.ok(propertyContractResponse);
    }

    @GetMapping
    public ResponseEntity<Page<PropertyContractResponse>> findAll(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<PropertyContractResponse> propertyContractsResponse = propertyContractFinderService.findAll(pageable);

        return ResponseEntity.ok(propertyContractsResponse);
    }
}
