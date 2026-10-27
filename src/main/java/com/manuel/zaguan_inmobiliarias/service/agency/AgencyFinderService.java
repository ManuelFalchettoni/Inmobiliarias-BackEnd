package com.manuel.zaguan_inmobiliarias.service.agency;

import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.exception.agency.AgencyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.agency.AgencyMapper;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class AgencyFinderService {
    private final JpaAgencyRepository jpaAgencyRepository;
    private final AgencyMapper agencyMapper;
    private final AgencyScopeService agencyScopeService;

    //Una inmobiliaria dada de baja responde 404. Para encontrarla hay que listar con
    //active=false, igual que en Property. Una inmobiliaria solo ve la suya: otra tambien da 404
    public AgencyResponse findById(Long id){
        if (!agencyScopeService.canAccess(id)) {
            throw new AgencyNotFoundException(id);
        }
        Agency agency = jpaAgencyRepository.findByIdAndActiveTrue(id)
                .orElseThrow(()-> new AgencyNotFoundException(id));
        return agencyMapper.toResponse(agency);
    }

    public Agency findAgency(Long id){
        return jpaAgencyRepository.findById(id)
                .orElseThrow(()-> new AgencyNotFoundException(id));
    }
}
