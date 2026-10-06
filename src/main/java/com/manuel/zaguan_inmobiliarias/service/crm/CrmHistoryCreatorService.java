package com.manuel.zaguan_inmobiliarias.service.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.CrmHistoryRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmHistoryResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmHistory;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmPropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.user.UserNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.crm.CrmHistoryMapper;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmHistoryRepository;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//Solo alta: el historial es un registro de lo que paso, no se edita ni se borra
@Service
@AllArgsConstructor
public class CrmHistoryCreatorService {
    private final JpaCrmHistoryRepository jpaCrmHistoryRepository;
    private final JpaCrmPropertyRepository jpaCrmPropertyRepository;
    private final JpaUserRepository jpaUserRepository;
    private final CrmHistoryMapper crmHistoryMapper;

    @Transactional
    public CrmHistoryResponse create(Long crmPropertyId, CrmHistoryRequest request){
        //Ids sueltos, sin FK: se controla aca que existan. El agente tiene que estar activo
        if (!jpaCrmPropertyRepository.existsById(crmPropertyId)) {
            throw new CrmPropertyNotFoundException(crmPropertyId);
        }
        if (!jpaUserRepository.existsByIdAndActiveTrue(request.getUserId())) {
            throw new UserNotFoundException(request.getUserId());
        }

        CrmHistory history = crmHistoryMapper.toEntity(request);
        history.setCrmPropertyId(crmPropertyId);
        return crmHistoryMapper.toResponse(jpaCrmHistoryRepository.save(history));
    }
}
