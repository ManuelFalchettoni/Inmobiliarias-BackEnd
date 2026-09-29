package com.manuel.zaguan_inmobiliarias.mapper.property;

import com.manuel.zaguan_inmobiliarias.dto.request.property.PropertyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertyResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.entity.property.photo.PropertyPhoto;
import com.manuel.zaguan_inmobiliarias.entity.property.price.PropertyPrice;
import com.manuel.zaguan_inmobiliarias.mapper.property.photo.PropertyPhotoMapper;
import com.manuel.zaguan_inmobiliarias.mapper.property.price.PropertyPriceMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PropertyMapper {
    private final PropertyPhotoMapper propertyPhotoMapper;
    private final PropertyPriceMapper propertyPriceMapper;

    public PropertyMapper(PropertyPhotoMapper propertyPhotoMapper, PropertyPriceMapper propertyPriceMapper){
        this.propertyPhotoMapper = propertyPhotoMapper;
        this.propertyPriceMapper = propertyPriceMapper;
    }
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
        response.setProvince(property.getProvince());
        response.setCounty(property.getCounty());
        response.setCity(property.getCity());
        response.setLatitude(property.getLatitude());
        response.setLongitude(property.getLongitude());
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
            response.setPhotos(photos.stream().map(propertyPhotoMapper::toResponse).toList());
        }

        List<PropertyPrice> prices = property.getPrices();
        if (prices != null) {
            response.setPrices(prices.stream().map(propertyPriceMapper::toResponse).toList());
        }

        return response;
    }

    private void copyFields(PropertyRequest request, Property property) {
        property.setAddress(request.getAddress());
        property.setType(request.getType());
        property.setProvince(request.getProvince());
        property.setCounty(request.getCounty());
        property.setCity(request.getCity());
        property.setLatitude(request.getLatitude());
        property.setLongitude(request.getLongitude());
        property.setYear(request.getYear());
        property.setRooms(request.getRooms());
        property.setSize(request.getSize());
        property.setCondition(request.getCondition());
        property.setOccupancy(request.getOccupancy());
        property.setFloorNumber(request.getFloorNumber());
    }
}
