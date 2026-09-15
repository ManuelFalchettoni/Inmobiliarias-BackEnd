package com.manuel.zaguan_inmobiliarias.service.agency;

import com.manuel.zaguan_inmobiliarias.dto.request.agency.AgencyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.exception.agency.AgencyAlreadyExistsException;
import com.manuel.zaguan_inmobiliarias.exception.agency.AgencyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.agency.AgencyMapper;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AgencyUpdaterService {
    private final JpaAgencyRepository jpaAgencyRepository;
    private final AgencyMapper agencyMapper;
    private final PasswordEncoder passwordEncoder;

    public AgencyResponse update (Long id, AgencyRequest agencyRequest){
        Agency toUpdate = jpaAgencyRepository.findById(id)
                .orElseThrow(() -> new AgencyNotFoundException(id));

        //Se busca el valor en otras inmobiliarias: la que se edita puede mantener sus datos
        if (jpaAgencyRepository.existsByCuitAndIdNot(agencyRequest.getCuit(), id)) {
            throw new AgencyAlreadyExistsException("Cuit already registered: " + agencyRequest.getCuit());
        }
        if (jpaAgencyRepository.existsByCompanyNameAndIdNot(agencyRequest.getCompanyName(), id)) {
            throw new AgencyAlreadyExistsException("Company name already registered: " + agencyRequest.getCompanyName());
        }
        if (jpaAgencyRepository.existsByEmailAndIdNot(agencyRequest.getEmail(), id)) {
            throw new AgencyAlreadyExistsException("Email already registered: " + agencyRequest.getEmail());
        }
        if (jpaAgencyRepository.existsByPhoneNumberAndIdNot(agencyRequest.getPhoneNumber(), id)) {
            throw new AgencyAlreadyExistsException("Phone number already registered: " + agencyRequest.getPhoneNumber());
        }

        toUpdate.setCuit(agencyRequest.getCuit());
        toUpdate.setEmail(agencyRequest.getEmail());
        toUpdate.setCompanyName(agencyRequest.getCompanyName());
        toUpdate.setPublicName(agencyRequest.getPublicName());
        toUpdate.setPassword(passwordEncoder.encode(agencyRequest.getPassword()));
        toUpdate.setAddress(agencyRequest.getAddress());
        toUpdate.setSocials(agencyRequest.getSocials());
        toUpdate.setPhoneNumber(agencyRequest.getPhoneNumber());
        toUpdate.setWebURL(agencyRequest.getWebURL());
        toUpdate.setStatus(agencyRequest.getStatus());

        //updatedAt lo pone @UpdateTimestamp al flushear
        return agencyMapper.toResponse(jpaAgencyRepository.saveAndFlush(toUpdate));
    }
}
