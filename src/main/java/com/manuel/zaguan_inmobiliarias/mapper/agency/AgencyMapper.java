package com.manuel.zaguan_inmobiliarias.mapper.agency;

import com.manuel.zaguan_inmobiliarias.dto.request.agency.AgencyRequest;

import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.enums.agency.AgencyStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class AgencyMapper {
    public Agency toEntity(AgencyRequest agencyRequest, User user){
        Agency agency = new Agency();
        agency.setCompanyName(agencyRequest.getCompanyName());
        agency.setPublicName(agencyRequest.getPublicName());
        agency.setAddress(agencyRequest.getAddress());
        agency.setSocials(agencyRequest.getSocials());
        agency.setUser(user);
        agency.setWebURL(agencyRequest.getWebURL());
        agency.setStatus(AgencyStatus.PENDING);

        LocalDateTime now = LocalDateTime.now();
        agency.setCreatedAt(now);
        agency.setUpdatedAt(now);

        return agency;
    }

    public AgencyResponse toResponse(Agency agency){
        AgencyResponse agencyResponse = new AgencyResponse();
        agencyResponse.setCompanyName(agency.getCompanyName());
        agencyResponse.setPublicName(agency.getPublicName());
        agencyResponse.setAddress(agency.getAddress());
        agencyResponse.setSocials(agency.getSocials());
        agencyResponse.setOwnerId(agency.getUser().getId());
        agencyResponse.setWebURL(agency.getWebURL());
        agencyResponse.setStatus(agency.getStatus());
        agencyResponse.setCreatedAt(agency.getCreatedAt());
        agencyResponse.setUpdatedAt(agency.getUpdatedAt());

        return agencyResponse;
    }
}
