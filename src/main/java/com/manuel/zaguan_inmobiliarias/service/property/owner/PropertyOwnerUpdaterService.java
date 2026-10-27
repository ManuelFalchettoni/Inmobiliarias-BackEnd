package com.manuel.zaguan_inmobiliarias.service.property.owner;

import com.manuel.zaguan_inmobiliarias.dto.request.property.owner.PropertyOwnerRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.owner.PropertyOwnerResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Owner.PropertyOwner;
import com.manuel.zaguan_inmobiliarias.exception.people.PeopleNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.owner.PropertyOwnerNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.owner.PropertyOwnerMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.owner.JpaPropertyOwnerRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PropertyOwnerUpdaterService {
    private final JpaPropertyOwnerRepository jpaPropertyOwnerRepository;
    private final PropertyOwnerMapper propertyOwnerMapper;
    private final AgencyScopeService agencyScopeService;

    @Transactional
    public PropertyOwnerResponse update(Long id,PropertyOwnerRequest propertyOwnerRequest){
        PropertyOwner toUpdate = jpaPropertyOwnerRepository.findById(id)
                .orElseThrow( () -> new PropertyOwnerNotFoundException(id));

        //Un vinculo de otra inmobiliaria responde 404, igual que uno que no existe
        if (!agencyScopeService.canAccessProperty(toUpdate.getPropertyId())) {
            throw new PropertyOwnerNotFoundException(id);
        }

        //La persona y la propiedad nuevas tambien tienen que ser de la inmobiliaria
        if(!agencyScopeService.canAccessPeople(propertyOwnerRequest.getPeopleId())){
            throw  new PeopleNotFoundException(propertyOwnerRequest.getPeopleId());
        }

        if(!agencyScopeService.canAccessProperty(propertyOwnerRequest.getPropertyId())){
            throw new PropertyNotFoundException(propertyOwnerRequest.getPropertyId());
        }

        toUpdate.setPropertyId(propertyOwnerRequest.getPropertyId());
        toUpdate.setPeopleId(propertyOwnerRequest.getPeopleId());
        toUpdate.setComments(propertyOwnerRequest.getComments());

        return propertyOwnerMapper.toResponse(jpaPropertyOwnerRepository.save(toUpdate));
    }
}
