package com.manuel.zaguan_inmobiliarias.service.property.contract;

import com.manuel.zaguan_inmobiliarias.dto.request.property.contract.PropertyContractRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.contract.PropertyContractResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.contract.PropertyContract;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractStatus;
import com.manuel.zaguan_inmobiliarias.exception.property.contract.PropertyContractNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.contract.PropertyContractOverlapException;
import com.manuel.zaguan_inmobiliarias.mapper.property.contract.PropertyContractMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.contract.JpaPropertyContractRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PropertyContractUpdaterService {
    private final JpaPropertyContractRepository jpaPropertyContractRepository;
    private final PropertyContractMapper propertyContractMapper;
    private final AgencyScopeService agencyScopeService;

    public PropertyContractResponse update(Long id, PropertyContractRequest propertyContractRequest){
        PropertyContract toUpdate = jpaPropertyContractRepository.findById(id)
                .orElseThrow( () -> new PropertyContractNotFoundException(id));

        //Un contrato de otra inmobiliaria responde 404, igual que uno que no existe
        if (!agencyScopeService.canAccessProperty(toUpdate.getPropertyId())) {
            throw new PropertyContractNotFoundException(id);
        }

        //Mismo control que en el alta, excluyendo el propio contrato para que no choque consigo mismo
        if (propertyContractRequest.getStatus() == ContractStatus.ACTIVE
                && jpaPropertyContractRepository.existsOverlappingActive(
                        toUpdate.getPropertyId(),
                        propertyContractRequest.getType(),
                        propertyContractRequest.getStartDate(),
                        propertyContractRequest.getEndDate(),
                        id)) {
            throw new PropertyContractOverlapException(toUpdate.getPropertyId());
        }

        toUpdate.setType(propertyContractRequest.getType());
        toUpdate.setStatus(propertyContractRequest.getStatus());
        toUpdate.setAmount(propertyContractRequest.getAmount());
        toUpdate.setCurrency(propertyContractRequest.getCurrency());
        toUpdate.setStartDate(propertyContractRequest.getStartDate());
        toUpdate.setEndDate(propertyContractRequest.getEndDate());
        toUpdate.setDocumentURL(propertyContractRequest.getDocumentURL());

        return propertyContractMapper.toResponse(jpaPropertyContractRepository.save(toUpdate));
    }
}
