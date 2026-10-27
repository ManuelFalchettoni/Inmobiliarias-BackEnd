package com.manuel.zaguan_inmobiliarias.service.crm;

import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmAlertResponse;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmAlertNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmPropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.crm.CrmAlertMapper;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmAlertRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private void requireCrmProperty(Long crmPropertyId){
        if (!agencyScopeService.canAccessCrmProperty(crmPropertyId)) {
            throw new CrmPropertyNotFoundException(crmPropertyId);
        }
    }
}
