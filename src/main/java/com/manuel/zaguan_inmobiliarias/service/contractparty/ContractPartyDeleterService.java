package com.manuel.zaguan_inmobiliarias.service.contractparty;

import com.manuel.zaguan_inmobiliarias.entity.contractparty.ContractParty;
import com.manuel.zaguan_inmobiliarias.exception.contractparty.ContractPartyNotFoundException;
import com.manuel.zaguan_inmobiliarias.repository.contractparty.JpaContractPartyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//Borrado fisico, como PropertyOwner: es solo el vinculo entre una persona y un contrato
@Service
@AllArgsConstructor
public class ContractPartyDeleterService {
    private final JpaContractPartyRepository jpaContractPartyRepository;

    @Transactional
    public void delete(Long id){
        ContractParty contractParty = jpaContractPartyRepository.findById(id)
                .orElseThrow(() -> new ContractPartyNotFoundException(id));

        jpaContractPartyRepository.delete(contractParty);
    }
}
