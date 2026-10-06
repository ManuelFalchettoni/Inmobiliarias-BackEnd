package com.manuel.zaguan_inmobiliarias.service.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.CrmPropertyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmPropertyResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmProperty;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmPropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.people.PeopleNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.user.UserNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.crm.CrmPropertyMapper;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.people.JpaPeopleRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//No hay baja: un lead que no avanza pasa a la etapa LOST desde aca
@Service
@AllArgsConstructor
public class CrmPropertyUpdaterService {
    private final JpaCrmPropertyRepository jpaCrmPropertyRepository;
    private final JpaPropertyRepository jpaPropertyRepository;
    private final JpaPeopleRepository jpaPeopleRepository;
    private final JpaUserRepository jpaUserRepository;
    private final CrmPropertyMapper crmPropertyMapper;

    @Transactional
    public CrmPropertyResponse update(Long id, CrmPropertyRequest request){
        CrmProperty toUpdate = jpaCrmPropertyRepository.findById(id)
                .orElseThrow(() -> new CrmPropertyNotFoundException(id));

        //Los mismos controles que en el alta: el PUT puede reasignar el lead a otro agente
        if (!jpaPropertyRepository.existsByIdAndActiveTrue(request.getPropertyId())) {
            throw new PropertyNotFoundException(request.getPropertyId());
        }
        if (!jpaPeopleRepository.existsById(request.getPeopleId())) {
            throw new PeopleNotFoundException(request.getPeopleId());
        }
        if (!jpaUserRepository.existsByIdAndActiveTrue(request.getUserId())) {
            throw new UserNotFoundException(request.getUserId());
        }

        crmPropertyMapper.updateEntity(request, toUpdate);
        //updatedAt lo pone @UpdateTimestamp al flushear
        return crmPropertyMapper.toResponse(jpaCrmPropertyRepository.saveAndFlush(toUpdate));
    }
}
