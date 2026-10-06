package com.manuel.zaguan_inmobiliarias.service.crm;

import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmHistoryResponse;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmHistoryNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmPropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.crm.CrmHistoryMapper;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmHistoryRepository;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmPropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CrmHistoryFinderService {
    private final JpaCrmHistoryRepository jpaCrmHistoryRepository;
    private final JpaCrmPropertyRepository jpaCrmPropertyRepository;
    private final CrmHistoryMapper crmHistoryMapper;

    //Sin paginar, como las ofertas: es la linea de tiempo de un solo lead
    public List<CrmHistoryResponse> findByCrmProperty(Long crmPropertyId){
        requireCrmProperty(crmPropertyId);
        return jpaCrmHistoryRepository.findByCrmPropertyIdOrderByCreatedAtDesc(crmPropertyId)
                .stream()
                .map(crmHistoryMapper::toResponse)
                .toList();
    }

    public CrmHistoryResponse findById(Long crmPropertyId, Long historyId){
        requireCrmProperty(crmPropertyId);
        return jpaCrmHistoryRepository.findByIdAndCrmPropertyId(historyId, crmPropertyId)
                .map(crmHistoryMapper::toResponse)
                .orElseThrow(() -> new CrmHistoryNotFoundException(historyId));
    }

    private void requireCrmProperty(Long crmPropertyId){
        if (!jpaCrmPropertyRepository.existsById(crmPropertyId)) {
            throw new CrmPropertyNotFoundException(crmPropertyId);
        }
    }
}
