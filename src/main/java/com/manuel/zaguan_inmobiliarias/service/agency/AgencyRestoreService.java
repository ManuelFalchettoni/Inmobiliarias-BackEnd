package com.manuel.zaguan_inmobiliarias.service.agency;

import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.exception.agency.AgencyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.agency.AgencyMapper;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AgencyRestoreService {
    private final JpaAgencyRepository jpaAgencyRepository;
    private final AgencyMapper agencyMapper;

    @Transactional
    public AgencyResponse restore(Long id){
        //findById pelado: si la inmobiliaria ya estaba activa no hace nada y devuelve 200 igual
        Agency agency = jpaAgencyRepository.findById(id)
                .orElseThrow(() -> new AgencyNotFoundException(id));

        agency.setActive(true);

        //Flush para que updatedAt salga actualizado en la respuesta
        jpaAgencyRepository.saveAndFlush(agency);
        return agencyMapper.toResponse(agency);
    }
}
