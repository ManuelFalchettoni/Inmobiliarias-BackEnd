package com.manuel.zaguan_inmobiliarias.service.agency;

import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.exception.agency.AgencyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.agency.AgencyMapper;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import com.manuel.zaguan_inmobiliarias.storage.PhotoStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class AgencyIconUpdaterService {
    private final JpaAgencyRepository jpaAgencyRepository;
    private final PhotoStorage photoStorage;
    private final AgencyMapper agencyMapper;

    //Sube el logo y reemplaza el anterior si habia. Los usuarios de la inmobiliaria lo ven
    //solos: UserMapper lo busca por agencyId
    @Transactional
    public AgencyResponse update(Long id, MultipartFile file) {
        Agency agency = jpaAgencyRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new AgencyNotFoundException(id));

        String oldKey = agency.getIcon();

        //Valida vacio, extension y content-type
        String newKey = photoStorage.store(file);

        try {
            agency.setIcon(newKey);
            //Flush para que un error de base salte antes de borrar el logo viejo
            Agency saved = jpaAgencyRepository.saveAndFlush(agency);

            photoStorage.delete(oldKey);

            return agencyMapper.toResponse(saved);
        } catch (RuntimeException e) {
            photoStorage.delete(newKey);
            throw e;
        }
    }
}
