package com.manuel.zaguan_inmobiliarias.service.property.contract;

import com.manuel.zaguan_inmobiliarias.dto.request.property.contract.PropertyContractRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.contract.PropertyContractResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.contract.PropertyContract;
import com.manuel.zaguan_inmobiliarias.mapper.property.contract.PropertyContractMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.contract.JpaPropertyContractRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PropertyContractUpdaterService {
    private final JpaPropertyContractRepository jpaPropertyContractRepository;
    private final PropertyContractMapper propertyContractMapper;

    public PropertyContractResponse update(Long id, PropertyContractRequest propertyContractRequest){
        PropertyContract toUpdate = jpaPropertyContractRepository.findById(id)
                .orElseThrow( () -> new PropertyContractNotFoundException(id));

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
