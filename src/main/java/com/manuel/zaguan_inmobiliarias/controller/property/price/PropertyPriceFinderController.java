package com.manuel.zaguan_inmobiliarias.controller.property.price;

import com.manuel.zaguan_inmobiliarias.dto.response.property.price.PropertyPriceResponse;
import com.manuel.zaguan_inmobiliarias.service.property.price.PropertyPriceFinderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/properties/{propertyId}/prices")
public class PropertyPriceFinderController {

    private final PropertyPriceFinderService propertyPriceFinderService;

    public PropertyPriceFinderController(PropertyPriceFinderService propertyPriceFinderService){
        this.propertyPriceFinderService = propertyPriceFinderService;
    }

    @GetMapping
    public ResponseEntity<List<PropertyPriceResponse>> findByProperty(@PathVariable Long propertyId){
        return ResponseEntity.ok(propertyPriceFinderService.findByProperty(propertyId));
    }

    @GetMapping("/{priceId}")
    public ResponseEntity<PropertyPriceResponse> findById(@PathVariable Long propertyId,
                                                          @PathVariable Long priceId){
        return ResponseEntity.ok(propertyPriceFinderService.findById(propertyId, priceId));
    }
}
