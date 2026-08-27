package com.manuel.zaguan_inmobiliarias.exception.property.photo;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PropertyPhotoNotFoundException extends RuntimeException {

    private final Long propertyPhotoId;

    public PropertyPhotoNotFoundException(Long propertyPhotoId) {
        super("Property photo not found with id: " + propertyPhotoId);
        this.propertyPhotoId = propertyPhotoId;
    }
}
