package com.manuel.zaguan_inmobiliarias.mapper.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.OfferRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.OfferResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.Offer;
import org.springframework.stereotype.Component;

@Component
public class OfferMapper {

    //El crmPropertyId no se copia aca: sale de la URL y lo asigna OfferCreatorService
    public Offer toEntity(OfferRequest request){
        Offer offer = new Offer();
        updateEntity(request, offer);
        return offer;
    }

    public void updateEntity(OfferRequest request, Offer offer){
        offer.setAmount(request.getAmount());
        offer.setCurrency(request.getCurrency());
        offer.setStatus(request.getStatus());
    }

    public OfferResponse toResponse(Offer offer){
        OfferResponse response = new OfferResponse();
        response.setId(offer.getId());
        response.setCrmPropertyId(offer.getCrmPropertyId());
        response.setAmount(offer.getAmount());
        response.setCurrency(offer.getCurrency());
        response.setStatus(offer.getStatus());
        response.setCreatedAt(offer.getCreatedAt());
        response.setUpdatedAt(offer.getUpdatedAt());
        return response;
    }
}
