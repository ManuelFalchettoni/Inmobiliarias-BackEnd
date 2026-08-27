package com.manuel.zaguan_inmobiliarias.exception.property.photo;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PhotoNotFoundException extends RuntimeException {
    private final Long photoId;

    public PhotoNotFoundException (Long photoId){
        super("Photo not found: " + photoId);
        this.photoId = photoId;
    }

    public PhotoNotFoundException(String message){
        super(message);
        this.photoId = null;
    }
}
