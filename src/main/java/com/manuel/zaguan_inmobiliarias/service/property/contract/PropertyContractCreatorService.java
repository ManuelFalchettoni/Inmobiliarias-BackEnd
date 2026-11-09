package com.manuel.zaguan_inmobiliarias.service.property.contract;

import com.manuel.zaguan_inmobiliarias.dto.request.property.contract.PropertyContractRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.contract.PropertyContractResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.contract.PropertyContract;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractStatus;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.contract.PropertyContractOverlapException;
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
        //propertyId es un id suelto, sin FK: la propiedad tiene que existir, estar activa y ser de la inmobiliaria.
        //Una archivada responde 404, igual que en precios y CRM
        if (!agencyScopeService.canAccessActiveProperty(propertyContractRequest.getPropertyId())) {
            throw new PropertyNotFoundException(propertyContractRequest.getPropertyId());
        }

        //Solo los ACTIVE se controlan: uno FINISHED o CANCELLED puede cargarse en cualquier periodo
        if (propertyContractRequest.getStatus() == ContractStatus.ACTIVE
                && jpaPropertyContractRepository.existsOverlappingActive(
                        propertyContractRequest.getPropertyId(),
                        propertyContractRequest.getType(),
                        propertyContractRequest.getStartDate(),
                        propertyContractRequest.getEndDate(),
                        null)) {
            throw new PropertyContractOverlapException(propertyContractRequest.getPropertyId());
        }

        PropertyContract propertyContract = propertyContractMapper.toEntity(propertyContractRequest);

        return propertyContractMapper.toResponse(jpaPropertyContractRepository.save(propertyContract));
    }
}
