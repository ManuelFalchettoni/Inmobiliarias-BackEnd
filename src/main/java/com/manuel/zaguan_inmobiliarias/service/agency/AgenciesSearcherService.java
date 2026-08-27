package com.manuel.zaguan_inmobiliarias.service.agency;

import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.mapper.agency.AgencyMapper;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AgenciesSearcherService {
    private final JpaAgencyRepository jpaAgencyRepository;
    private final AgencyMapper agencyMapper;

    public Page<AgencyResponse> findAll (Pageable pageable){
        Page<Agency> agencies = jpaAgencyRepository.findAll(pageable);

        return agencies.map(
                agencyMapper::toResponse
        );
    }
}
