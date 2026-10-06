package com.manuel.zaguan_inmobiliarias.service.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.CrmPropertyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmPropertyResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmProperty;
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

@Service
@AllArgsConstructor
public class CrmPropertyCreatorService {
    private final JpaCrmPropertyRepository jpaCrmPropertyRepository;
    private final JpaPropertyRepository jpaPropertyRepository;
    private final JpaPeopleRepository jpaPeopleRepository;
    private final JpaUserRepository jpaUserRepository;
    private final CrmPropertyMapper crmPropertyMapper;

    @Transactional
    public CrmPropertyResponse create(CrmPropertyRequest request){
        //Los ids son sueltos, sin FK: si no se controlan aca el lead queda apuntando a la nada.
        //Porque la relacion no es con anotacion de JPA
        //Propiedad y agente tienen que estar activos
        if (!jpaPropertyRepository.existsByIdAndActiveTrue(request.getPropertyId())) {
            throw new PropertyNotFoundException(request.getPropertyId());
        }
        if (!jpaPeopleRepository.existsById(request.getPeopleId())) {
            throw new PeopleNotFoundException(request.getPeopleId());
        }
        if (!jpaUserRepository.existsByIdAndActiveTrue(request.getUserId())) {
            throw new UserNotFoundException(request.getUserId());
        }

        CrmProperty crmProperty = crmPropertyMapper.toEntity(request);
        return crmPropertyMapper.toResponse(jpaCrmPropertyRepository.save(crmProperty));
    }
}
