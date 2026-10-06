package com.manuel.zaguan_inmobiliarias.service.crm;

import com.manuel.zaguan_inmobiliarias.entity.crm.CrmAlert;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmAlertNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmPropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmAlertRepository;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmPropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//Borrado fisico, como las ofertas: un recordatorio que ya no sirve no se restaura
@Service
@AllArgsConstructor
public class CrmAlertDeleterService {
    private final JpaCrmAlertRepository jpaCrmAlertRepository;
    private final JpaCrmPropertyRepository jpaCrmPropertyRepository;

    @Transactional
    public void delete(Long crmPropertyId, Long alertId){
        if (!jpaCrmPropertyRepository.existsById(crmPropertyId)) {
            throw new CrmPropertyNotFoundException(crmPropertyId);
        }

        CrmAlert alert = jpaCrmAlertRepository.findByIdAndCrmPropertyId(alertId, crmPropertyId)
                .orElseThrow(() -> new CrmAlertNotFoundException(alertId));

        jpaCrmAlertRepository.delete(alert);
    }
}
