package com.manuel.zaguan_inmobiliarias.service.agency;

import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.exception.agency.AgencyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.agency.AgencyMapper;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AgencyFinderService {
    private final JpaAgencyRepository jpaAgencyRepository;
    private final AgencyMapper agencyMapper;

    public AgencyResponse findById(Long id){
        Agency agency = jpaAgencyRepository.findById(id)
                .orElseThrow(()-> new AgencyNotFoundException(id));
        return agencyMapper.toResponse(agency);
    }

    public Agency findAgency(Long id){
        return jpaAgencyRepository.findById(id)
                .orElseThrow(()-> new AgencyNotFoundException(id));
    }

    public Page<AgencyResponse> findAll (Pageable pageable){
        Page<Agency> agencies = jpaAgencyRepository.findAll(pageable);

        return agencies.map(
                agencyMapper::toResponse
        );
    }
}
