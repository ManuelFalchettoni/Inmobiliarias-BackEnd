package com.manuel.zaguan_inmobiliarias.service.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.CrmAlertRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmAlertResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmAlert;
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
public class CrmAlertCreatorService {
    private final JpaCrmAlertRepository jpaCrmAlertRepository;
    private final CrmAlertMapper crmAlertMapper;
    private final AgencyScopeService agencyScopeService;

    @Transactional
    public CrmAlertResponse create(Long crmPropertyId, CrmAlertRequest request){
        //Ids sueltos, sin FK: se controla aca que existan. El agente tiene que estar activo
        if (!agencyScopeService.canAccessCrmProperty(crmPropertyId)) {
            throw new CrmPropertyNotFoundException(crmPropertyId);
        }
        if (!agencyScopeService.canAccessActiveUser(request.getUserId())) {
            throw new UserNotFoundException(request.getUserId());
        }

        CrmAlert alert = crmAlertMapper.toEntity(request);
        alert.setCrmPropertyId(crmPropertyId);
        return crmAlertMapper.toResponse(jpaCrmAlertRepository.save(alert));
    }
}
