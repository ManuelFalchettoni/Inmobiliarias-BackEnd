package com.manuel.zaguan_inmobiliarias.controller.property.owner;

import com.manuel.zaguan_inmobiliarias.dto.request.property.owner.PropertyOwnerRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.owner.PropertyOwnerResponse;
import com.manuel.zaguan_inmobiliarias.service.property.owner.PropertyOwnerCreatorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/property_owners")
@AllArgsConstructor
public class PropertyOwnerCreatorController {
    private final PropertyOwnerCreatorService propertyOwnerCreatorService;

    @PostMapping
    public ResponseEntity<PropertyOwnerResponse> create(@Valid @RequestBody PropertyOwnerRequest propertyOwnerRequest){
        PropertyOwnerResponse propertyOwnerResponse = propertyOwnerCreatorService.create(propertyOwnerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(propertyOwnerResponse);
    }
}
