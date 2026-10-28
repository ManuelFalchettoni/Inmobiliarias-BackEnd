package com.manuel.zaguan_inmobiliarias.service.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.CrmAlertRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmAlertResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmAlert;
import com.manuel.zaguan_inmobiliarias.enums.crm.CrmAlertStatus;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmAlertNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmPropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.user.UserNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.crm.CrmAlertMapper;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmAlertRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CrmAlertUpdaterService {
    private final JpaCrmAlertRepository jpaCrmAlertRepository;
    private final CrmAlertMapper crmAlertMapper;
    private final AgencyScopeService agencyScopeService;

    //Editar el mensaje, la fecha o pasarla a otro agente. El status no se toca aca
    @Transactional
    public CrmAlertResponse update(Long crmPropertyId, Long alertId, CrmAlertRequest request){
        CrmAlert alert = findAlert(crmPropertyId, alertId);

        //El PUT puede pasarle la alerta a otro agente
        if (!agencyScopeService.canAccessActiveUser(request.getUserId())) {
            throw new UserNotFoundException(request.getUserId());
        }

        crmAlertMapper.updateEntity(request, alert);
        return crmAlertMapper.toResponse(jpaCrmAlertRepository.saveAndFlush(alert));
    }

    //Dar la alerta por terminada: pasa a DONE y deja de salir en upcoming
    @Transactional
    public CrmAlertResponse complete(Long crmPropertyId, Long alertId){
        CrmAlert alert = findAlert(crmPropertyId, alertId);
        alert.setStatus(CrmAlertStatus.DONE);
        return crmAlertMapper.toResponse(jpaCrmAlertRepository.saveAndFlush(alert));
    }

    private CrmAlert findAlert(Long crmPropertyId, Long alertId){
        if (!agencyScopeService.canAccessCrmProperty(crmPropertyId)) {
            throw new CrmPropertyNotFoundException(crmPropertyId);
        }

        //Por id y por lead: una alerta de otro lead no se edita desde esta URL
        return jpaCrmAlertRepository.findByIdAndCrmPropertyId(alertId, crmPropertyId)
                .orElseThrow(() -> new CrmAlertNotFoundException(alertId));
    }
}
