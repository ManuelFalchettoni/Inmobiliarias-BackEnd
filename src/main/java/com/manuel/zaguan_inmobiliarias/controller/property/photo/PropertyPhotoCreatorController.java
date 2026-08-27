package com.manuel.zaguan_inmobiliarias.controller.property.photo;

import com.manuel.zaguan_inmobiliarias.dto.response.property.photo.PropertyPhotoResponse;
import com.manuel.zaguan_inmobiliarias.service.property.photo.PropertyPhotoCreatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/properties/{propertyId}/photos")
public class PropertyPhotoCreatorController {

    private final PropertyPhotoCreatorService propertyPhotoCreatorService;

    public PropertyPhotoCreatorController(PropertyPhotoCreatorService propertyPhotoCreatorService){
        this.propertyPhotoCreatorService = propertyPhotoCreatorService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<PropertyPhotoResponse>> upload(@PathVariable Long propertyId,
                                                              @RequestPart("files") List<MultipartFile> files){
        List<PropertyPhotoResponse> responses = propertyPhotoCreatorService.upload(propertyId, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }
}
