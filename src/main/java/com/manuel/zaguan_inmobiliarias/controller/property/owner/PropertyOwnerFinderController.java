package com.manuel.zaguan_inmobiliarias.controller.property.owner;

import com.manuel.zaguan_inmobiliarias.dto.response.property.owner.PropertyOwnerResponse;
import com.manuel.zaguan_inmobiliarias.service.property.owner.PropertyOwnerFinderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/property_owners")
@AllArgsConstructor
public class PropertyOwnerFinderController {
    private final PropertyOwnerFinderService propertyOwnerFinderService;

    @GetMapping("/{id}")
    public ResponseEntity<PropertyOwnerResponse> findById(@PathVariable Long id){
        PropertyOwnerResponse propertyOwnerResponse = propertyOwnerFinderService.findById(id);

        return ResponseEntity.ok(propertyOwnerResponse);
    }

    @GetMapping
    public ResponseEntity<Page<PropertyOwnerResponse>> findAll(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<PropertyOwnerResponse> propertyOwnerResponses = propertyOwnerFinderService.findAll(pageable);

        return ResponseEntity.ok(propertyOwnerResponses);
    }
}
