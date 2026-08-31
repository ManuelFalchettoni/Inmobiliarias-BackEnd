package com.manuel.zaguan_inmobiliarias.service.agency;

import com.manuel.zaguan_inmobiliarias.dto.request.agency.AgencyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.exception.agency.AgencyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.agency.AgencyMapper;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AgencyUpdaterService {
    private final JpaAgencyRepository jpaAgencyRepository;
    private final AgencyMapper agencyMapper;

    public AgencyResponse update (Long id, AgencyRequest agencyRequest){
        Agency toUpdate = jpaAgencyRepository.findById(id)
                .orElseThrow(() -> new AgencyNotFoundException(id));
        toUpdate.setCuit(agencyRequest.getCuit());
        toUpdate.setEmail(agencyRequest.getEmail());
        toUpdate.setCompanyName(agencyRequest.getCompanyName());
        toUpdate.setPublicName(agencyRequest.getPublicName());
        toUpdate.setEmail(agencyRequest.getEmail());
        toUpdate.setPassword(agencyRequest.getPassword());
        toUpdate.setAddress(agencyRequest.getAddress());
        toUpdate.setSocials(agencyRequest.getSocials());
        toUpdate.setPhoneNumber(agencyRequest.getPhoneNumber());
        toUpdate.setWebURL(agencyRequest.getWebURL());
        toUpdate.setStatus(agencyRequest.getStatus());

        //updatedAt lo pone @UpdateTimestamp al flushear
        return agencyMapper.toResponse(jpaAgencyRepository.saveAndFlush(toUpdate));
    }
}
