

package com.manuel.zaguan_inmobiliarias.controller.property.price;

import com.manuel.zaguan_inmobiliarias.dto.request.property.price.PropertyPriceRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.price.PropertyPriceResponse;
import com.manuel.zaguan_inmobiliarias.service.property.price.PropertyPriceCreatorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/properties/{propertyId}/prices")
public class PropertyPriceCreatorController {

    private final PropertyPriceCreatorService propertyPriceCreatorService;

    public PropertyPriceCreatorController(PropertyPriceCreatorService propertyPriceCreatorService){
        this.propertyPriceCreatorService = propertyPriceCreatorService;
    }

    @PostMapping
    public ResponseEntity<PropertyPriceResponse> create(@PathVariable Long propertyId,
                                                        @Valid @RequestBody PropertyPriceRequest request){
        PropertyPriceResponse response = propertyPriceCreatorService.create(propertyId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
