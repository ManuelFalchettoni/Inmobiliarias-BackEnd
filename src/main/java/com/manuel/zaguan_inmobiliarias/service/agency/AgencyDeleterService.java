package com.manuel.zaguan_inmobiliarias.service.agency;

import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.enums.agency.AgencyStatus;
import com.manuel.zaguan_inmobiliarias.enums.user.UserRol;
import com.manuel.zaguan_inmobiliarias.exception.agency.AgencyAlreadyDeletedException;
import com.manuel.zaguan_inmobiliarias.exception.agency.NotAgencyOwnerException;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AgencyDeleterService {
    private final JpaAgencyRepository jpaAgencyRepository;
    private final AgencyFinderService agencyFinderService;
    private final JpaUserRepository jpaUserRepository;

    public void delete (Long id){
        agencyFinderService.findById(id);
        jpaAgencyRepository.deleteById(id);
    }

    @Transactional
    public void deleteAgency(Long id, Long userId){
        Agency agency = agencyFinderService.findAgency(id);

        if(agency.getStatus() == AgencyStatus.DELETED){
           throw new AgencyAlreadyDeletedException("Agency with id: " + agency.getId() + " is already deleted.");
        }
        if(!agency.getUser().getId().equals(userId)){
            throw new NotAgencyOwnerException("Only the agency owner can delete the account.");
        }
        agency.setStatus(AgencyStatus.DELETED);
        jpaAgencyRepository.save(agency);
        List<User> affectedUsers = jpaUserRepository.findByAgency_Id(id);
        for (User user : affectedUsers) {
            user.setAgency(null);
            user.setRol(UserRol.USER);
        }
        jpaUserRepository.saveAll(affectedUsers);
    }

}
