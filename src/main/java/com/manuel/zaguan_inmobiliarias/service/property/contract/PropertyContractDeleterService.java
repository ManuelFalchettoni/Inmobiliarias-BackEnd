package com.manuel.zaguan_inmobiliarias.service.property.contract;

import com.manuel.zaguan_inmobiliarias.entity.property.contract.PropertyContract;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractStatus;
import com.manuel.zaguan_inmobiliarias.exception.property.contract.PropertyContractNotFoundException;
import com.manuel.zaguan_inmobiliarias.repository.property.contract.JpaPropertyContractRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PropertyContractDeleterService {
    private final JpaPropertyContractRepository jpaPropertyContractRepository;

    //Se hace un soft delete cambiando el status del contract a cancelled
    public void delete(Long id){
        PropertyContract toDelete = jpaPropertyContractRepository.findById(id)
                .orElseThrow( () -> new PropertyContractNotFoundException(id));

        toDelete.setStatus(ContractStatus.CANCELLED);

        jpaPropertyContractRepository.save(toDelete);
    }
}
