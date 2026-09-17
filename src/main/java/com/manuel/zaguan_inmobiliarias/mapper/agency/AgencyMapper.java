package com.manuel.zaguan_inmobiliarias.mapper.agency;

import com.manuel.zaguan_inmobiliarias.dto.request.agency.AgencyRequest;
import com.manuel.zaguan_inmobiliarias.dto.request.agency.AgencyUpdateRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import org.springframework.stereotype.Component;


@Component
public class AgencyMapper {
    public Agency toEntity(AgencyRequest agencyRequest){
        Agency agency = new Agency();
        agency.setCuit(agencyRequest.getCuit());
        agency.setEmail(agencyRequest.getEmail());
        agency.setCompanyName(agencyRequest.getCompanyName());
        agency.setPublicName(agencyRequest.getPublicName());
        //La contraseña no se copia aca: la hashea AgencyCreatorService
        agency.setAddress(agencyRequest.getAddress());
        agency.setSocials(agencyRequest.getSocials());
        agency.setPhoneNumber(agencyRequest.getPhoneNumber());
        agency.setWebURL(agencyRequest.getWebURL());
        agency.setStatus(agencyRequest.getStatus());
        agency.setActive(true);

        return agency;
    }

    //La contraseña y active no se tocan al editar: cada uno tiene su endpoint
    public void updateEntity(AgencyUpdateRequest agencyUpdateRequest, Agency agency){
        agency.setCuit(agencyUpdateRequest.getCuit());
        agency.setEmail(agencyUpdateRequest.getEmail());
        agency.setCompanyName(agencyUpdateRequest.getCompanyName());
        agency.setPublicName(agencyUpdateRequest.getPublicName());
        agency.setAddress(agencyUpdateRequest.getAddress());
        agency.setSocials(agencyUpdateRequest.getSocials());
        agency.setPhoneNumber(agencyUpdateRequest.getPhoneNumber());
        agency.setWebURL(agencyUpdateRequest.getWebURL());
        agency.setStatus(agencyUpdateRequest.getStatus());
    }

    public AgencyResponse toResponse(Agency agency){
        AgencyResponse agencyResponse = new AgencyResponse();
        agencyResponse.setId(agency.getId());
        agencyResponse.setCuit(agency.getCuit());
        agencyResponse.setEmail(agency.getEmail());
        agencyResponse.setCompanyName(agency.getCompanyName());
        agencyResponse.setPublicName(agency.getPublicName());
        agencyResponse.setAddress(agency.getAddress());
        agencyResponse.setSocials(agency.getSocials());
        agencyResponse.setPhoneNumber(agency.getPhoneNumber());
        agencyResponse.setWebURL(agency.getWebURL());
        agencyResponse.setStatus(agency.getStatus());
        agencyResponse.setActive(agency.getActive());
        agencyResponse.setCreatedAt(agency.getCreatedAt());
        agencyResponse.setUpdatedAt(agency.getUpdatedAt());

        return agencyResponse;
    }
}
