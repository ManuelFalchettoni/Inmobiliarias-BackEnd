package com.manuel.zaguan_inmobiliarias.controller.property.photo;

import com.manuel.zaguan_inmobiliarias.dto.response.property.photo.PropertyPhotoResponse;
import com.manuel.zaguan_inmobiliarias.service.property.photo.PropertyPhotoFinderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/properties/{propertyId}/photos")
public class PropertyPhotoFinderController {

    private final PropertyPhotoFinderService propertyPhotoFinderService;

    public PropertyPhotoFinderController(PropertyPhotoFinderService propertyPhotoFinderService){
        this.propertyPhotoFinderService = propertyPhotoFinderService;
    }

    @GetMapping
    public ResponseEntity<List<PropertyPhotoResponse>> findByProperty(@PathVariable Long propertyId){
        return ResponseEntity.ok(propertyPhotoFinderService.findByProperty(propertyId));
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<PropertyPhotoResponse> findById(@PathVariable Long propertyId,
                                                          @PathVariable Long photoId){
        return ResponseEntity.ok(propertyPhotoFinderService.findById(propertyId, photoId));
    }
}
