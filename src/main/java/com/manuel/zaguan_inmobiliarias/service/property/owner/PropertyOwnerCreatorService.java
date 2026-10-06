package com.manuel.zaguan_inmobiliarias.service.property.owner;

import com.manuel.zaguan_inmobiliarias.dto.request.property.owner.PropertyOwnerRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.owner.PropertyOwnerResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Owner.PropertyOwner;
import com.manuel.zaguan_inmobiliarias.exception.people.PeopleNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.owner.PropertyOwnerAlreadyExistsException;
import com.manuel.zaguan_inmobiliarias.mapper.property.owner.PropertyOwnerMapper;
import com.manuel.zaguan_inmobiliarias.repository.people.JpaPeopleRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.owner.JpaPropertyOwnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PropertyOwnerCreatorService {
    private final JpaPropertyRepository jpaPropertyRepository;
    private final JpaPeopleRepository jpaPeopleRepository;
    private final JpaPropertyOwnerRepository jpaPropertyOwnerRepository;
    private final PropertyOwnerMapper propertyOwnerMapper;

    @Transactional
    public PropertyOwnerResponse create(PropertyOwnerRequest propertyOwnerRequest){
        if(jpaPropertyOwnerRepository.existsByPropertyIdAndPeopleId(propertyOwnerRequest.getPropertyId(), propertyOwnerRequest.getPeopleId())){
            throw new PropertyOwnerAlreadyExistsException(propertyOwnerRequest.getPropertyId(),propertyOwnerRequest.getPeopleId());
        }

        if (!jpaPropertyRepository.existsById(propertyOwnerRequest.getPropertyId()) ){
            throw new PropertyNotFoundException(propertyOwnerRequest.getPropertyId());
        }

        if (!jpaPeopleRepository.existsById(propertyOwnerRequest.getPeopleId())){
            throw new PeopleNotFoundException(propertyOwnerRequest.getPeopleId());
        }

        PropertyOwner propertyOwner = propertyOwnerMapper.toEntity(propertyOwnerRequest);

        return propertyOwnerMapper.toResponse(jpaPropertyOwnerRepository.save(propertyOwner));
    }
}
