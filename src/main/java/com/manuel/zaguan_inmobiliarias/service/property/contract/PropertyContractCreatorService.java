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
public class PropertyContractCreatorService {
    private final JpaPropertyContractRepository jpaPropertyContractRepository;
    private final PropertyContractMapper propertyContractMapper;

    public PropertyContractResponse create(PropertyContractRequest propertyContractRequest){
        PropertyContract propertyContract = propertyContractMapper.toEntity(propertyContractRequest);

        return propertyContractMapper.toResponse(jpaPropertyContractRepository.save(propertyContract));
    }
}
