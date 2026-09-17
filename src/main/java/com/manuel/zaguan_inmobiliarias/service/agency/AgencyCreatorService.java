package com.manuel.zaguan_inmobiliarias.service.agency;

import com.manuel.zaguan_inmobiliarias.dto.request.agency.AgencyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.exception.agency.AgencyAlreadyExistsException;
import com.manuel.zaguan_inmobiliarias.mapper.agency.AgencyMapper;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AgencyCreatorService {

    private final JpaAgencyRepository jpaAgencyRepository;
    private final AgencyMapper agencyMapper;
    private final PasswordEncoder passwordEncoder;

    //Los exists y el save en la misma transaccion. Igual queda la red del handler de
    //DataIntegrityViolationException, para dos altas simultaneas con el mismo dato
    @Transactional
    public AgencyResponse create(AgencyRequest agencyRequest){
        if (jpaAgencyRepository.existsByCuit(agencyRequest.getCuit())) {
            throw new AgencyAlreadyExistsException("Cuit already registered: " + agencyRequest.getCuit());
        }
        if (jpaAgencyRepository.existsByCompanyName(agencyRequest.getCompanyName())) {
            throw new AgencyAlreadyExistsException("Company name already registered: " + agencyRequest.getCompanyName());
        }
        if (jpaAgencyRepository.existsByEmail(agencyRequest.getEmail())) {
            throw new AgencyAlreadyExistsException("Email already registered: " + agencyRequest.getEmail());
        }
        if (jpaAgencyRepository.existsByPhoneNumber(agencyRequest.getPhoneNumber())) {
            throw new AgencyAlreadyExistsException("Phone number already registered: " + agencyRequest.getPhoneNumber());
        }

        Agency agency = agencyMapper.toEntity(agencyRequest);
        //La contraseña nunca se guarda como llega: se guarda el hash de BCrypt
        agency.setPassword(passwordEncoder.encode(agencyRequest.getPassword()));

        return agencyMapper.toResponse(jpaAgencyRepository.save(agency));
    }


}
