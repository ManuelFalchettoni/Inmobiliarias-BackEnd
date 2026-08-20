package com.manuel.zaguan_inmobiliarias.mapper.property;

import com.manuel.zaguan_inmobiliarias.dto.request.property.PropertyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertyResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.entity.property.photo.PropertyPhoto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PropertyMapper {

    public Property toEntity(PropertyRequest request) {
        Property property = new Property();

        property.setIdAgency(request.getIdAgency());
        property.setActive(true);
        copyFields(request, property);

        return property;
    }

    public void updateEntity(PropertyRequest request, Property property) {
        copyFields(request, property);
    }

    public PropertyResponse toResponse(Property property) {
        PropertyResponse response = new PropertyResponse();

        response.setId(property.getId());
        response.setAddress(property.getAddress());
        response.setActive(property.getActive());
        response.setType(property.getType());
        response.setLocation(property.getLocation());
        response.setIdAgency(property.getIdAgency());
        response.setYear(property.getYear());
        response.setCreatedAt(property.getCreatedAt());
        response.setUpdatedAt(property.getUpdatedAt());
        response.setRooms(property.getRooms());
        response.setSize(property.getSize());
        response.setCondition(property.getCondition());
        response.setOccupancy(property.getOccupancy());
        response.setFloorNumber(property.getFloorNumber());

        List<PropertyPhoto> photos = property.getPhotos();
        if (photos != null) {
            response.setPhotoUrls(photos.stream().map(PropertyPhoto::getUrl).toList());
        }

        return response;
    }

    private void copyFields(PropertyRequest request, Property property) {
        property.setAddress(request.getAddress());
        property.setType(request.getType());
        property.setLocation(request.getLocation());
        property.setYear(request.getYear());
        property.setRooms(request.getRooms());
        property.setSize(request.getSize());
        property.setCondition(request.getCondition());
        property.setOccupancy(request.getOccupancy());
        property.setFloorNumber(request.getFloorNumber());
    }
}
