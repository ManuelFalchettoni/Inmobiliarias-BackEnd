package com.manuel.zaguan_inmobiliarias.mapper.property.photo;

import com.manuel.zaguan_inmobiliarias.dto.response.property.photo.PropertyPhotoResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.photo.PropertyPhoto;
import com.manuel.zaguan_inmobiliarias.storage.PhotoStorage;
import org.springframework.stereotype.Component;

@Component
public class PropertyPhotoMapper {

    private final PhotoStorage photoStorage;

    public PropertyPhotoMapper(PhotoStorage photoStorage) {
        this.photoStorage = photoStorage;
    }

    public PropertyPhotoResponse toResponse(PropertyPhoto photo){
        PropertyPhotoResponse response = new PropertyPhotoResponse(
                photo.getId(),
                photoStorage.urlOf(photo.getObjectKey()), //en la base esta solo el objectKey
                photo.getPhotoName(),
                photo.getPosition()
        );
        return response;
    }
}
