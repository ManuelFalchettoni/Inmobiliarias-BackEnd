package com.manuel.zaguan_inmobiliarias.controller.property;

import com.manuel.zaguan_inmobiliarias.dto.request.property.PropertyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertyResponse;
import com.manuel.zaguan_inmobiliarias.service.property.PropertyUpdaterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/properties")
public class PropertyPutController {
    private final PropertyUpdaterService propertyUpdaterService;

    public PropertyPutController(PropertyUpdaterService propertyUpdaterService){
        this.propertyUpdaterService = propertyUpdaterService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody PropertyRequest request){
        return ResponseEntity.ok(propertyUpdaterService.update(id, request));
    }
}
