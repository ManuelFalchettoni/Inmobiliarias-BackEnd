package com.manuel.zaguan_inmobiliarias.exception.property.photo;

import lombok.Getter;

@Getter
public class PropertyPhotoNotFoundException extends RuntimeException {

    private final Long propertyPhotoId;

    public PropertyPhotoNotFoundException(Long propertyPhotoId) {
        super("Property photo not found with id: " + propertyPhotoId);
        this.propertyPhotoId = propertyPhotoId;
    }
}
