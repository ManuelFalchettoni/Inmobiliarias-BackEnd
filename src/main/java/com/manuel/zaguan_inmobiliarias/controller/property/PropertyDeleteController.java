package com.manuel.zaguan_inmobiliarias.controller.property;

import com.manuel.zaguan_inmobiliarias.service.property.PropertyDeleteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/properties")
public class PropertyDeleteController {
    private final PropertyDeleteService propertyDeleteService;

    public PropertyDeleteController(PropertyDeleteService propertyDeleteService){
        this.propertyDeleteService = propertyDeleteService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        propertyDeleteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
