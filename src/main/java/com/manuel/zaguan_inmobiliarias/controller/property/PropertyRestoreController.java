package com.manuel.zaguan_inmobiliarias.controller.property;

import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertyResponse;
import com.manuel.zaguan_inmobiliarias.service.property.PropertyRestoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/properties")
public class PropertyRestoreController {
    private final PropertyRestoreService propertyRestoreService;

    public PropertyRestoreController(PropertyRestoreService propertyRestoreService){
        this.propertyRestoreService = propertyRestoreService;
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<PropertyResponse> restore(@PathVariable Long id){
        return ResponseEntity.ok(propertyRestoreService.restore(id));
    }
}
