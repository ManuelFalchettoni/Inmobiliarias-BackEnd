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
import org.springframework.data.domain.PageImpl;
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
        return toResponsePage(jpaCrmPropertyRepository.findAllByAgency(scopedAgencyId, pageable));
    }

    //Los leads de un agente: el agente tiene que ser de la inmobiliaria del usuario
    public Page<CrmPropertyResponse> findByUser(Long userId, Pageable pageable){
        if (!agencyScopeService.canAccessActiveUser(userId)) {
            throw new UserNotFoundException(userId);
        }
        return toResponsePage(jpaCrmPropertyRepository.findAllByUserId(userId, pageable));
    }

    //toResponses y no map(toResponse): asi propiedades, personas y agentes de la pagina se traen
    //con una consulta cada uno, en vez de una por lead
    private Page<CrmPropertyResponse> toResponsePage(Page<CrmProperty> crmProperties){
        return new PageImpl<>(crmPropertyMapper.toResponses(crmProperties.getContent()),
                crmProperties.getPageable(), crmProperties.getTotalElements());
    }
}
