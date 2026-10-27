package com.manuel.zaguan_inmobiliarias.service.crm;

import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmPropertyResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmProperty;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmPropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.user.UserNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.crm.CrmPropertyMapper;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmPropertyRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CrmPropertyFinderService {
    private final JpaCrmPropertyRepository jpaCrmPropertyRepository;
    private final CrmPropertyMapper crmPropertyMapper;
    private final AgencyScopeService agencyScopeService;

    //Un lead de otra inmobiliaria responde 404, igual que uno que no existe
    public CrmPropertyResponse findById(Long id){
        CrmProperty crmProperty = jpaCrmPropertyRepository.findById(id)
                .orElseThrow(() -> new CrmPropertyNotFoundException(id));

        if (!agencyScopeService.canAccessProperty(crmProperty.getPropertyId())) {
            throw new CrmPropertyNotFoundException(id);
        }
        return crmPropertyMapper.toResponse(crmProperty);
    }

    //Sin agencyId por parametro: el admin ve todos, una inmobiliaria los de sus propiedades
    public Page<CrmPropertyResponse> findAll(Pageable pageable){
        Long scopedAgencyId = agencyScopeService.resolveAgencyId(null);
        return jpaCrmPropertyRepository.findAllByAgency(scopedAgencyId, pageable)
                .map(crmPropertyMapper::toResponse);
    }

    //Los leads de un agente: el agente tiene que ser de la inmobiliaria del usuario
    public Page<CrmPropertyResponse> findByUser(Long userId, Pageable pageable){
        if (!agencyScopeService.canAccessActiveUser(userId)) {
            throw new UserNotFoundException(userId);
        }
        return jpaCrmPropertyRepository.findAllByUserId(userId, pageable)
                .map(crmPropertyMapper::toResponse);
    }
}
