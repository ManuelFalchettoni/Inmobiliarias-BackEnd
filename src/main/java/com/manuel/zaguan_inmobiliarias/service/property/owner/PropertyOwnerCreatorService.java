package com.manuel.zaguan_inmobiliarias.service.property.owner;

import com.manuel.zaguan_inmobiliarias.dto.request.property.owner.PropertyOwnerRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.owner.PropertyOwnerResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Owner.PropertyOwner;
import com.manuel.zaguan_inmobiliarias.exception.people.PeopleNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.owner.PropertyOwnerAlreadyExistsException;
import com.manuel.zaguan_inmobiliarias.mapper.property.owner.PropertyOwnerMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.owner.JpaPropertyOwnerRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PropertyOwnerCreatorService {
    private final JpaPropertyOwnerRepository jpaPropertyOwnerRepository;
    private final PropertyOwnerMapper propertyOwnerMapper;
    private final AgencyScopeService agencyScopeService;

    @Transactional
    public PropertyOwnerResponse create(PropertyOwnerRequest propertyOwnerRequest){
        if(jpaPropertyOwnerRepository.existsByPropertyIdAndPeopleId(propertyOwnerRequest.getPropertyId(), propertyOwnerRequest.getPeopleId())){
            throw new PropertyOwnerAlreadyExistsException(propertyOwnerRequest.getPropertyId(),propertyOwnerRequest.getPeopleId());
        }

        //La propiedad y la persona tienen que existir y ser de la inmobiliaria del usuario
        if (!agencyScopeService.canAccessProperty(propertyOwnerRequest.getPropertyId())){
            throw new PropertyNotFoundException(propertyOwnerRequest.getPropertyId());
        }

        if (!agencyScopeService.canAccessPeople(propertyOwnerRequest.getPeopleId())){
            throw new PeopleNotFoundException(propertyOwnerRequest.getPeopleId());
        }

        PropertyOwner propertyOwner = propertyOwnerMapper.toEntity(propertyOwnerRequest);

        return propertyOwnerMapper.toResponse(jpaPropertyOwnerRepository.save(propertyOwner));
    }
}
