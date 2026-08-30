package com.manuel.zaguan_inmobiliarias.service.agency;

import com.manuel.zaguan_inmobiliarias.dto.request.agency.AgencyUpdaterRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.enums.agency.AgencyStatus;
import com.manuel.zaguan_inmobiliarias.exception.agency.AgencyAlreadyDeletedException;
import com.manuel.zaguan_inmobiliarias.exception.agency.NotAgencyOwnerException;
import com.manuel.zaguan_inmobiliarias.mapper.agency.AgencyMapper;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import com.manuel.zaguan_inmobiliarias.service.user.UserFinderService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AgencyUpdaterService {
    private final JpaAgencyRepository jpaAgencyRepository;
    private final AgencyMapper agencyMapper;
    private final AgencyFinderService agencyFinderService;
    private final UserFinderService userFinderService;


    @Transactional
    public AgencyResponse update (Long id, AgencyUpdaterRequest agencyRequest, Long userId){
        Agency toUpdate = agencyFinderService.findAgency(id);

        if (!toUpdate.getUser().getId().equals(userId)){
            throw  new NotAgencyOwnerException("Only the owner of the agency can update.");
        }
        if (toUpdate.getStatus() == AgencyStatus.DELETED){
            throw  new AgencyAlreadyDeletedException("The agency with id: " + toUpdate.getId() + " is deleted.");
        }
        User user = userFinderService.find(userId);

        toUpdate.setCompanyName(agencyRequest.getCompanyName());
        toUpdate.setPublicName(agencyRequest.getPublicName());
        toUpdate.setAddress(agencyRequest.getAddress());
        toUpdate.setSocials(agencyRequest.getSocials());
        toUpdate.setWebURL(agencyRequest.getWebURL());
        LocalDateTime now = LocalDateTime.now();
        toUpdate.setUpdatedAt(now);

        return agencyMapper.toResponse(jpaAgencyRepository.save(toUpdate));
    }
}
