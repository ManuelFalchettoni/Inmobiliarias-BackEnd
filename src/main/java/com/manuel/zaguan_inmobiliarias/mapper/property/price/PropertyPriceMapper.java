package com.manuel.zaguan_inmobiliarias.mapper.property.price;

import com.manuel.zaguan_inmobiliarias.dto.request.property.price.PropertyPriceRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.price.PropertyPriceResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.price.PropertyPrice;
import org.springframework.stereotype.Component;

@Component
public class PropertyPriceMapper {

    //La propiedad no se copia aca: la asigna PropertyPriceCreatorService
    public PropertyPrice toEntity(PropertyPriceRequest request) {
        PropertyPrice price = new PropertyPrice();
        updateEntity(request, price);
        return price;
    }

    public void updateEntity(PropertyPriceRequest request, PropertyPrice price) {
        price.setOperationType(request.getOperationType());
        price.setCurrency(request.getCurrency());
        price.setAmount(request.getAmount());
    }

    public PropertyPriceResponse toResponse(PropertyPrice price) {
        return new PropertyPriceResponse(
                price.getId(),
                price.getOperationType(),
                price.getCurrency(),
                price.getAmount()
        );
    }
}
