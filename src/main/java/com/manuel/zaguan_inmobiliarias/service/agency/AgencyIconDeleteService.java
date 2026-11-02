package com.manuel.zaguan_inmobiliarias.service.agency;

import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.exception.agency.AgencyNotFoundException;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import com.manuel.zaguan_inmobiliarias.storage.PhotoStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AgencyIconDeleteService {
    private final JpaAgencyRepository jpaAgencyRepository;
    private final PhotoStorage photoStorage;

    @Transactional
    public void delete(Long id) {
        Agency agency = jpaAgencyRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new AgencyNotFoundException(id));

        String objectKey = agency.getIcon();

        //Sin logo no hay nada que borrar: responde 204 igual
        if (objectKey == null) {
            return;
        }

        agency.setIcon(null);
        jpaAgencyRepository.saveAndFlush(agency);

        photoStorage.delete(objectKey);
    }
}
