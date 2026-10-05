package com.manuel.zaguan_inmobiliarias.service.property.owner;

import com.manuel.zaguan_inmobiliarias.dto.response.property.owner.PropertyOwnerResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Owner.PropertyOwner;
import com.manuel.zaguan_inmobiliarias.exception.property.owner.PropertyOwnerNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.owner.PropertyOwnerMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.owner.JpaPropertyOwnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PropertyOwnerFinderService {
    private final JpaPropertyOwnerRepository jpaPropertyOwnerRepository;
    private final PropertyOwnerMapper propertyOwnerMapper;

    public PropertyOwnerResponse findById(Long id){
        PropertyOwner propertyOwner = jpaPropertyOwnerRepository.findById(id)
                .orElseThrow( () -> new PropertyOwnerNotFoundException(id));

        return propertyOwnerMapper.toResponse(propertyOwner);
    }

    public Page<PropertyOwnerResponse> findAll(Pageable pageable){
        Page<PropertyOwner> propertyOwners = jpaPropertyOwnerRepository.findAll(pageable);

        return propertyOwners.map(
                propertyOwnerMapper :: toResponse
        );
    }
}
