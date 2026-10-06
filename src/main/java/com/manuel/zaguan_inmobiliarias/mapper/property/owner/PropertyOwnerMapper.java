package com.manuel.zaguan_inmobiliarias.mapper.property.owner;

import com.manuel.zaguan_inmobiliarias.dto.request.property.owner.PropertyOwnerRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.owner.PropertyOwnerResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Owner.PropertyOwner;

public class PropertyOwnerMapper {
    public PropertyOwner toEntity(PropertyOwnerRequest propertyOwnerRequest){
        PropertyOwner propertyOwner = new PropertyOwner();

        propertyOwner.setPropertyId(propertyOwnerRequest.getPropertyId());
        propertyOwner.setPeopleId(propertyOwnerRequest.getPeopleId());
        propertyOwner.setComments(propertyOwnerRequest.getComments());
        return propertyOwner;
    }


    public PropertyOwnerResponse toResponse(PropertyOwner propertyOwner){
        PropertyOwnerResponse propertyOwnerResponse = new PropertyOwnerResponse();

        propertyOwnerResponse.setId(propertyOwner.getId());
        propertyOwnerResponse.setPropertyId(propertyOwner.getPropertyId());
        propertyOwnerResponse.setPeopleId(propertyOwner.getPeopleId());
        propertyOwnerResponse.setComments(propertyOwner.getComments());

        return propertyOwnerResponse;
    }
}
