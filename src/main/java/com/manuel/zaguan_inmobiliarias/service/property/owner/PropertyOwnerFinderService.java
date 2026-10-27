package com.manuel.zaguan_inmobiliarias.service.property.owner;

import com.manuel.zaguan_inmobiliarias.dto.response.property.owner.PropertyOwnerResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Owner.PropertyOwner;
import com.manuel.zaguan_inmobiliarias.exception.property.owner.PropertyOwnerNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.owner.PropertyOwnerMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.owner.JpaPropertyOwnerRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PropertyOwnerFinderService {
    private final JpaPropertyOwnerRepository jpaPropertyOwnerRepository;
    private final PropertyOwnerMapper propertyOwnerMapper;
    private final AgencyScopeService agencyScopeService;

    //Un vinculo de otra inmobiliaria responde 404, igual que uno que no existe
    public PropertyOwnerResponse findById(Long id){
        PropertyOwner propertyOwner = jpaPropertyOwnerRepository.findById(id)
                .orElseThrow( () -> new PropertyOwnerNotFoundException(id));

        if (!agencyScopeService.canAccessProperty(propertyOwner.getPropertyId())) {
            throw new PropertyOwnerNotFoundException(id);
        }
        return propertyOwnerMapper.toResponse(propertyOwner);
    }

    //Sin agencyId por parametro: el admin ve todos, una inmobiliaria los de sus propiedades
    public Page<PropertyOwnerResponse> findAll(Pageable pageable){
        Long scopedAgencyId = agencyScopeService.resolveAgencyId(null);
        Page<PropertyOwner> propertyOwners = jpaPropertyOwnerRepository.findAllByAgency(scopedAgencyId, pageable);

        return propertyOwners.map(
                propertyOwnerMapper :: toResponse
        );
    }
}
