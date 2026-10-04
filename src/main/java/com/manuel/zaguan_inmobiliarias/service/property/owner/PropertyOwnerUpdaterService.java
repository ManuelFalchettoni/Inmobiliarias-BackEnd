package com.manuel.zaguan_inmobiliarias.service.property.owner;

import com.manuel.zaguan_inmobiliarias.dto.request.property.owner.PropertyOwnerRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.owner.PropertyOwnerResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Owner.PropertyOwner;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.owner.PropertyOwnerMapper;
import com.manuel.zaguan_inmobiliarias.repository.people.JpaPeopleRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.owner.JpaPropertyOwnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PropertyOwnerUpdaterService {
    private final JpaPropertyOwnerRepository jpaPropertyOwnerRepository;
    private final JpaPropertyRepository jpaPropertyRepository;
    private final JpaPeopleRepository jpaPeopleRepository;
    private final PropertyOwnerMapper propertyOwnerMapper;

    @Transactional
    public PropertyOwnerResponse update(Long id,PropertyOwnerRequest propertyOwnerRequest){
        PropertyOwner toUpdate = jpaPropertyOwnerRepository.findById(id)
                .orElseThrow( () -> new PropertyOwnerNotFoundException(id));

        if(!jpaPeopleRepository.existsById(propertyOwnerRequest.getPeopleId())){
            throw  new PeopleNotFoundException(propertyOwnerRequest.getPeopleId());
        }

        if(!jpaPropertyRepository.existsById(propertyOwnerRequest.getPropertyId())){
            throw new PropertyNotFoundException(propertyOwnerRequest.getPropertyId());
        }

        toUpdate.setPropertyId(propertyOwnerRequest.getPropertyId());
        toUpdate.setComments(propertyOwnerRequest.getComments());

        return propertyOwnerMapper.toResponse(jpaPropertyOwnerRepository.save(toUpdate));
    }
}
