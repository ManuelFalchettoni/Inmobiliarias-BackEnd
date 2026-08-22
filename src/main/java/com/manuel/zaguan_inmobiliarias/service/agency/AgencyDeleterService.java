package com.manuel.zaguan_inmobiliarias.service.agency;

import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AgencyDeleterService {
    private final JpaAgencyRepository jpaAgencyRepository;
    private final AgencyFinderService agencyFinderService;

    public void delete (Long id){
        agencyFinderService.findById(id);
        jpaAgencyRepository.deleteById(id);
    }

}
