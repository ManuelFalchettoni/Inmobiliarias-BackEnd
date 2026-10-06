package com.manuel.zaguan_inmobiliarias.controller.property.contract;

import com.manuel.zaguan_inmobiliarias.service.property.contract.PropertyContractDeleterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/property_contracts")
public class PropertyContractDeleteController {
    private final PropertyContractDeleterService propertyContractDeleterService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id){
        propertyContractDeleterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
