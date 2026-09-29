package com.manuel.zaguan_inmobiliarias.controller.property.price;

import com.manuel.zaguan_inmobiliarias.service.property.price.PropertyPriceDeleteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/properties/{propertyId}/prices")
public class PropertyPriceDeleteController {

    private final PropertyPriceDeleteService propertyPriceDeleteService;

    public PropertyPriceDeleteController(PropertyPriceDeleteService propertyPriceDeleteService){
        this.propertyPriceDeleteService = propertyPriceDeleteService;
    }

    @DeleteMapping("/{priceId}")
    public ResponseEntity<Void> delete(@PathVariable Long propertyId,
                                       @PathVariable Long priceId){
        propertyPriceDeleteService.delete(propertyId, priceId);
        return ResponseEntity.noContent().build();
    }
}
