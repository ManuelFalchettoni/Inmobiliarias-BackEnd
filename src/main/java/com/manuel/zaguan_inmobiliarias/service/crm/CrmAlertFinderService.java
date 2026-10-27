package com.manuel.zaguan_inmobiliarias.service.crm;

import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmAlertResponse;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmAlertNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmPropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.user.UserNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.crm.CrmAlertMapper;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmAlertRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CrmAlertFinderService {
    private final JpaCrmAlertRepository jpaCrmAlertRepository;
    private final CrmAlertMapper crmAlertMapper;
    private final AgencyScopeService agencyScopeService;

    //Sin paginar, como las ofertas: un lead tiene pocas alertas
    public List<CrmAlertResponse> findByCrmProperty(Long crmPropertyId){
        requireCrmProperty(crmPropertyId);
        return jpaCrmAlertRepository.findByCrmPropertyIdOrderByAlertDateAsc(crmPropertyId)
                .stream()
                .map(crmAlertMapper::toResponse)
                .toList();
    }

    public CrmAlertResponse findById(Long crmPropertyId, Long alertId){
        requireCrmProperty(crmPropertyId);
        return jpaCrmAlertRepository.findByIdAndCrmPropertyId(alertId, crmPropertyId)
                .map(crmAlertMapper::toResponse)
                .orElseThrow(() -> new CrmAlertNotFoundException(alertId));
    }

    //Las no leidas de aca a "days" dias, mas las vencidas sin leer: lo que el agente tiene pendiente.
    //Sin paginar: son las pendientes, no el historial
    public List<CrmAlertResponse> findUpcoming(int days, Long userId){
        if (userId != null && !agencyScopeService.canAccessActiveUser(userId)) {
            throw new UserNotFoundException(userId);
        }
        Long scopedAgencyId = agencyScopeService.resolveAgencyId(null);
        LocalDateTime until = LocalDateTime.now().plusDays(days);

        return jpaCrmAlertRepository.findUpcoming(scopedAgencyId, userId, until)
                .stream()
                .map(crmAlertMapper::toResponse)
                .toList();
    }

    private void requireCrmProperty(Long crmPropertyId){
        if (!agencyScopeService.canAccessCrmProperty(crmPropertyId)) {
            throw new CrmPropertyNotFoundException(crmPropertyId);
        }
    }
}
