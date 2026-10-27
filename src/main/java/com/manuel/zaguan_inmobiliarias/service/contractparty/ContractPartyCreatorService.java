package com.manuel.zaguan_inmobiliarias.service.contractparty;

import com.manuel.zaguan_inmobiliarias.dto.request.contractparty.ContractPartyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.contractparty.ContractPartyResponse;
import com.manuel.zaguan_inmobiliarias.entity.contractparty.ContractParty;
import com.manuel.zaguan_inmobiliarias.exception.people.PeopleNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.contract.PropertyContractNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.contractparty.ContractPartyMapper;
import com.manuel.zaguan_inmobiliarias.repository.contractparty.JpaContractPartyRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContractPartyCreatorService {
    private final JpaContractPartyRepository jpaContractPartyRepository;
    private final ContractPartyMapper contractPartyMapper;
    private final AgencyScopeService agencyScopeService;

    public ContractPartyResponse create(ContractPartyRequest contractPartyRequest){
        //La persona y el contrato tienen que existir y ser de la inmobiliaria del usuario
        if (!agencyScopeService.canAccessPeople(contractPartyRequest.getPeopleId())){
            throw new PeopleNotFoundException(contractPartyRequest.getPeopleId());
        }

        if (!agencyScopeService.canAccessContract(contractPartyRequest.getContractId())){
            throw new PropertyContractNotFoundException(contractPartyRequest.getContractId());
        }

        ContractParty contractParty = contractPartyMapper.toEntity(contractPartyRequest);

        return contractPartyMapper.toResponse(jpaContractPartyRepository.save(contractParty));
    }
}
