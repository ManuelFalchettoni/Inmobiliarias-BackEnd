package com.manuel.zaguan_inmobiliarias.mapper.agency;

import com.manuel.zaguan_inmobiliarias.dto.request.agency.AgencyRequest;
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
        agency.setEmail(agencyRequest.getEmail());
        agency.setPassword(agencyRequest.getPassword());
        agency.setAddress(agencyRequest.getAddress());
        agency.setSocials(agencyRequest.getSocials());
        agency.setPhoneNumber(agencyRequest.getPhoneNumber());
        agency.setWebURL(agencyRequest.getWebURL());
        agency.setStatus(agencyRequest.getStatus());

        return agency;
    }

    public AgencyResponse toResponse(Agency agency){
        AgencyResponse agencyResponse = new AgencyResponse();
        agencyResponse.setCuit(agency.getCuit());
        agencyResponse.setEmail(agency.getEmail());
        agencyResponse.setCompanyName(agency.getCompanyName());
        agencyResponse.setPublicName(agency.getPublicName());
        agencyResponse.setEmail(agency.getEmail());
        agencyResponse.setPassword(agency.getPassword());
        agencyResponse.setAddress(agency.getAddress());
        agencyResponse.setSocials(agency.getSocials());
        agencyResponse.setPhoneNumber(agency.getPhoneNumber());
        agencyResponse.setWebURL(agency.getWebURL());
        agencyResponse.setStatus(agency.getStatus());
        agencyResponse.setCreatedAt(agency.getCreatedAt());
        agencyResponse.setUpdatedAt(agency.getUpdatedAt());

        return agencyResponse;
    }
}
