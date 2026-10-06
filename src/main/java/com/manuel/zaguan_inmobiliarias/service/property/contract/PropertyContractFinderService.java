package com.manuel.zaguan_inmobiliarias.service.property.contract;

import com.manuel.zaguan_inmobiliarias.dto.response.property.contract.PropertyContractResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.contract.PropertyContract;
import com.manuel.zaguan_inmobiliarias.exception.property.contract.PropertyContractNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.contract.PropertyContractMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.contract.JpaPropertyContractRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PropertyContractFinderService {
    private final JpaPropertyContractRepository jpaPropertyContractRepository;
    private final PropertyContractMapper propertyContractMapper;

    public PropertyContractResponse findById(Long id){
        PropertyContract propertyContract = jpaPropertyContractRepository.findById(id)
                .orElseThrow( () -> new PropertyContractNotFoundException(id));

        return propertyContractMapper.toResponse(propertyContract);
    }

    public Page<PropertyContractResponse> findAll(Pageable pageable){
        Page<PropertyContract> propertyContracts = jpaPropertyContractRepository.findAll(pageable);

        return propertyContracts.map(
                propertyContractMapper :: toResponse
        );
    }
}
