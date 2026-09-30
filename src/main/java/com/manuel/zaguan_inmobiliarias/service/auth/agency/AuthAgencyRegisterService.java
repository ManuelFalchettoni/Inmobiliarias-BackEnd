package com.manuel.zaguan_inmobiliarias.service.auth.agency;

import com.manuel.zaguan_inmobiliarias.dto.request.auth.agency.AuthAgencyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.exception.auth.DuplicateResourceException;
import com.manuel.zaguan_inmobiliarias.mapper.agency.AgencyMapper;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthAgencyRegisterService {
    private final JpaAgencyRepository jpaAgencyRepository;
    private final AgencyMapper agencyMapper;
    private final PasswordEncoder passwordEncoder;

    public AgencyResponse agencyRegister (AuthAgencyRequest agencyRequest) {
        if (jpaAgencyRepository.existsAgencyByEmail(agencyRequest.getEmail())){
            throw new DuplicateResourceException("User with email: " + agencyRequest.getEmail() + " already exists.");
        }

        Agency agency = agencyMapper.toEntity(agencyRequest);
        agency.setPassword(passwordEncoder.encode(agencyRequest.getPassword()));

        return agencyMapper.toResponse(jpaAgencyRepository.save(agency));
    }
}
