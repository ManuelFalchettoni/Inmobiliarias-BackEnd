package com.manuel.zaguan_inmobiliarias.controller.property;

import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertyResponse;
import com.manuel.zaguan_inmobiliarias.service.property.PropertyArchiveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/properties")
public class PropertyArchiveController {
    private final PropertyArchiveService propertyArchiveService;

    public PropertyArchiveController(PropertyArchiveService propertyArchiveService){
        this.propertyArchiveService = propertyArchiveService;
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<PropertyResponse> archive(@PathVariable Long id){
        return ResponseEntity.ok(propertyArchiveService.archive(id));
    }
}
