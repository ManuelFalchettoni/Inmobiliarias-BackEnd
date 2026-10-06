package com.manuel.zaguan_inmobiliarias.service.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.CrmAlertRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmAlertResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmAlert;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmAlertNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmPropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.user.UserNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.crm.CrmAlertMapper;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmAlertRepository;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//Aca se marca una alerta como leida: isRead en true
@Service
@AllArgsConstructor
public class CrmAlertUpdaterService {
    private final JpaCrmAlertRepository jpaCrmAlertRepository;
    private final JpaCrmPropertyRepository jpaCrmPropertyRepository;
    private final JpaUserRepository jpaUserRepository;
    private final CrmAlertMapper crmAlertMapper;

    @Transactional
    public CrmAlertResponse update(Long crmPropertyId, Long alertId, CrmAlertRequest request){
        if (!jpaCrmPropertyRepository.existsById(crmPropertyId)) {
            throw new CrmPropertyNotFoundException(crmPropertyId);
        }

        //Por id y por lead: una alerta de otro lead no se edita desde esta URL
        CrmAlert alert = jpaCrmAlertRepository.findByIdAndCrmPropertyId(alertId, crmPropertyId)
                .orElseThrow(() -> new CrmAlertNotFoundException(alertId));

        //El PUT puede pasarle la alerta a otro agente
        if (!jpaUserRepository.existsByIdAndActiveTrue(request.getUserId())) {
            throw new UserNotFoundException(request.getUserId());
        }

        crmAlertMapper.updateEntity(request, alert);
        return crmAlertMapper.toResponse(jpaCrmAlertRepository.saveAndFlush(alert));
    }
}
