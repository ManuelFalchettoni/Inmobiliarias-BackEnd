package com.manuel.zaguan_inmobiliarias.service.agency;

import com.manuel.zaguan_inmobiliarias.dto.request.agency.AgencyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.enums.user.UserRol;
import com.manuel.zaguan_inmobiliarias.exception.agency.UserAlreadyHasAgencyException;
import com.manuel.zaguan_inmobiliarias.mapper.agency.AgencyMapper;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import com.manuel.zaguan_inmobiliarias.service.user.UserFinderService;
import com.manuel.zaguan_inmobiliarias.service.user.UserUpdaterService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AgencyCreatorService {
    private final JpaAgencyRepository jpaAgencyRepository;
    private final AgencyMapper agencyMapper;
    private final UserUpdaterService userUpdaterService;
    private final UserFinderService userFinderService;

    @Transactional
    public AgencyResponse register(AgencyRequest agencyRequest, Long userId){

        User user = userFinderService.find(userId);

        if (user.getAgency() != null) {
            throw new UserAlreadyHasAgencyException("User already has an agency with id: " + user.getAgency().getId());
        }

        Agency agency = agencyMapper.toEntity(agencyRequest, user);
        Agency saved = jpaAgencyRepository.save(agency);

        user.setRol(UserRol.AGENCY_ADMIN);
        user.setAgency(saved);
        userUpdaterService.updateUserInAgency(userId, user);

        return agencyMapper.toResponse(saved);
    }
}
