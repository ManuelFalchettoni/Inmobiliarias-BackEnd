package com.manuel.zaguan_inmobiliarias.mapper.property.photo;

import com.manuel.zaguan_inmobiliarias.dto.response.property.photo.PropertyPhotoResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.photo.PropertyPhoto;
import org.springframework.stereotype.Component;

@Component
public class PropertyPhotoMapper {

    public PropertyPhotoResponse toResponse(PropertyPhoto photo){
        PropertyPhotoResponse response = new PropertyPhotoResponse(
                photo.getId(),
                photo.getUrl(),
                photo.getPhotoName(),
                photo.getPosition()
        );
        return response;
    }
}
