package com.manuel.zaguan_inmobiliarias.service.contractparty;

import com.manuel.zaguan_inmobiliarias.entity.contractparty.ContractParty;
import com.manuel.zaguan_inmobiliarias.exception.contractparty.ContractPartyNotFoundException;
import com.manuel.zaguan_inmobiliarias.repository.contractparty.JpaContractPartyRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//Borrado fisico, como PropertyOwner: es solo el vinculo entre una persona y un contrato
@Service
@AllArgsConstructor
public class ContractPartyDeleterService {
    private final JpaContractPartyRepository jpaContractPartyRepository;
    private final AgencyScopeService agencyScopeService;

    @Transactional
    public void delete(Long id){
        ContractParty contractParty = jpaContractPartyRepository.findById(id)
                .orElseThrow(() -> new ContractPartyNotFoundException(id));

        //Una parte de otra inmobiliaria responde 404, igual que una que no existe
        if (!agencyScopeService.canAccessContract(contractParty.getContractId())) {
            throw new ContractPartyNotFoundException(id);
        }

        jpaContractPartyRepository.delete(contractParty);
    }
}
