package com.manuel.zaguan_inmobiliarias.controller.property.owner;

import com.manuel.zaguan_inmobiliarias.dto.request.property.owner.PropertyOwnerRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.owner.PropertyOwnerResponse;
import com.manuel.zaguan_inmobiliarias.service.property.owner.PropertyOwnerUpdaterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/property_owners")
@AllArgsConstructor
public class PropertyOwnerUpdaterController {
    private final PropertyOwnerUpdaterService propertyOwnerUpdaterService;

    @PutMapping("/{id}")
    public ResponseEntity<PropertyOwnerResponse> update(@PathVariable Long id,
                                                        @Valid @RequestBody PropertyOwnerRequest propertyOwnerRequest){
        PropertyOwnerResponse propertyOwnerResponse = propertyOwnerUpdaterService.update(id, propertyOwnerRequest);

        return ResponseEntity.ok(propertyOwnerResponse);
    }
}
