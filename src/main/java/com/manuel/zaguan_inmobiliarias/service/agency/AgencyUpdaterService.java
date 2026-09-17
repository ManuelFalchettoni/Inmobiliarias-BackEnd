package com.manuel.zaguan_inmobiliarias.service.agency;

import com.manuel.zaguan_inmobiliarias.dto.request.agency.AgencyUpdateRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.exception.agency.AgencyAlreadyExistsException;
import com.manuel.zaguan_inmobiliarias.exception.agency.AgencyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.agency.AgencyMapper;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AgencyUpdaterService {
    private final JpaAgencyRepository jpaAgencyRepository;
    private final AgencyMapper agencyMapper;

    @Transactional
    public AgencyResponse update (Long id, AgencyUpdateRequest agencyUpdateRequest){
        Agency toUpdate = jpaAgencyRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new AgencyNotFoundException(id));

        //Los controles van antes de los set: si la inmobiliaria ya tiene los datos nuevos, la
        //consulta exists haria flush y saltaria el error de la base antes que el nuestro.
        //Se busca el valor en otras inmobiliarias: la que se edita puede mantener sus datos
        if (jpaAgencyRepository.existsByCuitAndIdNot(agencyUpdateRequest.getCuit(), id)) {
            throw new AgencyAlreadyExistsException("Cuit already registered: " + agencyUpdateRequest.getCuit());
        }
        if (jpaAgencyRepository.existsByCompanyNameAndIdNot(agencyUpdateRequest.getCompanyName(), id)) {
            throw new AgencyAlreadyExistsException("Company name already registered: " + agencyUpdateRequest.getCompanyName());
        }
        if (jpaAgencyRepository.existsByEmailAndIdNot(agencyUpdateRequest.getEmail(), id)) {
            throw new AgencyAlreadyExistsException("Email already registered: " + agencyUpdateRequest.getEmail());
        }
        if (jpaAgencyRepository.existsByPhoneNumberAndIdNot(agencyUpdateRequest.getPhoneNumber(), id)) {
            throw new AgencyAlreadyExistsException("Phone number already registered: " + agencyUpdateRequest.getPhoneNumber());
        }

        agencyMapper.updateEntity(agencyUpdateRequest, toUpdate);

        //updatedAt lo pone @UpdateTimestamp al flushear
        return agencyMapper.toResponse(jpaAgencyRepository.saveAndFlush(toUpdate));
    }
}
