package com.manuel.zaguan_inmobiliarias.controller.property.owner;

import com.manuel.zaguan_inmobiliarias.service.property.owner.PropertyOwnerDeleterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/property_owners")
@AllArgsConstructor
public class PropertyOwnerDeleteController {
    private final PropertyOwnerDeleterService propertyOwnerDeleterService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        propertyOwnerDeleterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
