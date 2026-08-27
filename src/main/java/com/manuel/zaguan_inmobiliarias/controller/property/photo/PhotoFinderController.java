package com.manuel.zaguan_inmobiliarias.controller.property.photo;

import com.manuel.zaguan_inmobiliarias.dto.response.property.photo.PropertyPhotoResponse;
import com.manuel.zaguan_inmobiliarias.service.property.photo.PropertyPhotoFinderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(name = "photos")
public class PhotoFinderController {
    private final PropertyPhotoFinderService propertyPhotoFinderService;

    public PhotoFinderController (PropertyPhotoFinderService propertyPhotoFinderService){
        this.propertyPhotoFinderService = propertyPhotoFinderService;
    }

    public ResponseEntity<List<PropertyPhotoResponse>> find(Long propertyId){

    }
}
