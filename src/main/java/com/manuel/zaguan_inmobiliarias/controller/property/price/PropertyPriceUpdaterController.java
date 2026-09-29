package com.manuel.zaguan_inmobiliarias.controller.property.price;

import com.manuel.zaguan_inmobiliarias.dto.request.property.price.PropertyPriceRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.price.PropertyPriceResponse;
import com.manuel.zaguan_inmobiliarias.service.property.price.PropertyPriceUpdaterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/properties/{propertyId}/prices")
public class PropertyPriceUpdaterController {

    private final PropertyPriceUpdaterService propertyPriceUpdaterService;

    public PropertyPriceUpdaterController(PropertyPriceUpdaterService propertyPriceUpdaterService){
        this.propertyPriceUpdaterService = propertyPriceUpdaterService;
    }

    @PutMapping("/{priceId}")
    public ResponseEntity<PropertyPriceResponse> update(@PathVariable Long propertyId,
                                                        @PathVariable Long priceId,
                                                        @Valid @RequestBody PropertyPriceRequest request){
        return ResponseEntity.ok(propertyPriceUpdaterService.update(propertyId, priceId, request));
    }
}
