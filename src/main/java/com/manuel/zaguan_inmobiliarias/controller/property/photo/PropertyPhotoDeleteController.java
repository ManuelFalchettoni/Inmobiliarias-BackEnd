package com.manuel.zaguan_inmobiliarias.controller.property.photo;

import com.manuel.zaguan_inmobiliarias.service.property.photo.PropertyPhotoDeleteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/properties/{propertyId}/photos")
public class PropertyPhotoDeleteController {

    private final PropertyPhotoDeleteService propertyPhotoDeleteService;

    public PropertyPhotoDeleteController(PropertyPhotoDeleteService propertyPhotoDeleteService){
        this.propertyPhotoDeleteService = propertyPhotoDeleteService;
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> delete(@PathVariable Long propertyId,
                                       @PathVariable Long photoId){
        propertyPhotoDeleteService.delete(propertyId, photoId);
        return ResponseEntity.noContent().build();
    }
}
