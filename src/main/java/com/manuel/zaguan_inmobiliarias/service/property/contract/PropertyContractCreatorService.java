package com.manuel.zaguan_inmobiliarias.service.property.contract;

import com.manuel.zaguan_inmobiliarias.dto.request.property.contract.PropertyContractRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.contract.PropertyContractResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.contract.PropertyContract;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.contract.PropertyContractMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.contract.JpaPropertyContractRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PropertyContractCreatorService {
    private final JpaPropertyContractRepository jpaPropertyContractRepository;
    private final PropertyContractMapper propertyContractMapper;
    private final AgencyScopeService agencyScopeService;

    public PropertyContractResponse create(PropertyContractRequest propertyContractRequest){
        //propertyId es un id suelto, sin FK: la propiedad tiene que existir y ser de la inmobiliaria
        if (!agencyScopeService.canAccessProperty(propertyContractRequest.getPropertyId())) {
            throw new PropertyNotFoundException(propertyContractRequest.getPropertyId());
        }

        PropertyContract propertyContract = propertyContractMapper.toEntity(propertyContractRequest);

        return propertyContractMapper.toResponse(jpaPropertyContractRepository.save(propertyContract));
    }
}
