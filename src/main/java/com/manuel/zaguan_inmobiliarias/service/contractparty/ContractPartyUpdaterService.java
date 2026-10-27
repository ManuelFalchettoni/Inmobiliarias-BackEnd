package com.manuel.zaguan_inmobiliarias.service.contractparty;

import com.manuel.zaguan_inmobiliarias.dto.request.contractparty.ContractPartyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.contractparty.ContractPartyResponse;
import com.manuel.zaguan_inmobiliarias.entity.contractparty.ContractParty;
import com.manuel.zaguan_inmobiliarias.exception.contractparty.ContractPartyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.people.PeopleNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.contract.PropertyContractNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.contractparty.ContractPartyMapper;
import com.manuel.zaguan_inmobiliarias.repository.contractparty.JpaContractPartyRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContractPartyUpdaterService {
    private final JpaContractPartyRepository jpaContractPartyRepository;
    private final ContractPartyMapper contractPartyMapper;
    private final AgencyScopeService agencyScopeService;

    public ContractPartyResponse update(Long id, ContractPartyRequest contractPartyRequest){
        ContractParty toUpdate = jpaContractPartyRepository.findById(id)
                .orElseThrow(() -> new ContractPartyNotFoundException(id));

        //Una parte de otra inmobiliaria responde 404, igual que una que no existe
        if (!agencyScopeService.canAccessContract(toUpdate.getContractId())) {
            throw new ContractPartyNotFoundException(id);
        }

        //Como en el alta: la persona y el contrato nuevos tienen que ser de la inmobiliaria
        if (!agencyScopeService.canAccessPeople(contractPartyRequest.getPeopleId())){
            throw new PeopleNotFoundException(contractPartyRequest.getPeopleId());
        }

        if (!agencyScopeService.canAccessContract(contractPartyRequest.getContractId())){
            throw new PropertyContractNotFoundException(contractPartyRequest.getContractId());
        }

        toUpdate.setContractId(contractPartyRequest.getContractId());
        toUpdate.setPeopleId(contractPartyRequest.getPeopleId());
        toUpdate.setRole(contractPartyRequest.getRole());
        toUpdate.setComments(contractPartyRequest.getComments());

        return contractPartyMapper.toResponse(jpaContractPartyRepository.save(toUpdate));
    }
}
