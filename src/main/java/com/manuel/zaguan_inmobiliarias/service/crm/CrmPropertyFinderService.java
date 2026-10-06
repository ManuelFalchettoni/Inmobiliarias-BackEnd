package com.manuel.zaguan_inmobiliarias.service.crm;

import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmPropertyResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmProperty;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmPropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.crm.CrmPropertyMapper;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmPropertyRepository;
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

    public CrmPropertyResponse findById(Long id){
        CrmProperty crmProperty = jpaCrmPropertyRepository.findById(id)
                .orElseThrow(() -> new CrmPropertyNotFoundException(id));

        return crmPropertyMapper.toResponse(crmProperty);
    }

    public Page<CrmPropertyResponse> findAll(Pageable pageable){
        return jpaCrmPropertyRepository.findAll(pageable)
                .map(crmPropertyMapper::toResponse);
    }

    public Page<CrmPropertyResponse> findByUser(Long userId, Pageable pageable){
        return jpaCrmPropertyRepository.findAllByUserId(userId, pageable)
                .map(crmPropertyMapper::toResponse);
    }
}
