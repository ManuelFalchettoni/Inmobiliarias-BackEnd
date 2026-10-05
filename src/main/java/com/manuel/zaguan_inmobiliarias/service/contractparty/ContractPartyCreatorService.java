package com.manuel.zaguan_inmobiliarias.service.contractparty;

import com.manuel.zaguan_inmobiliarias.dto.request.contractparty.ContractPartyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.contractparty.ContractPartyResponse;
import com.manuel.zaguan_inmobiliarias.entity.contractparty.ContractParty;
import com.manuel.zaguan_inmobiliarias.exception.people.PeopleNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.contractparty.ContractPartyMapper;
import com.manuel.zaguan_inmobiliarias.repository.contractparty.JpaContractPartyRepository;
import com.manuel.zaguan_inmobiliarias.repository.people.JpaPeopleRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.contract.JpaPropertyContractRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContractPartyCreatorService {
    private final JpaContractPartyRepository jpaContractPartyRepository;
    private final JpaPeopleRepository jpaPeopleRepository;
    private final ContractPartyMapper contractPartyMapper;
    private final JpaPropertyContractRepository jpaPropertyContractRepository;

    public ContractPartyResponse create(ContractPartyRequest contractPartyRequest){
        if (!jpaPeopleRepository.existsById(contractPartyRequest.getPeopleId())){
            throw new PeopleNotFoundException(contractPartyRequest.getPeopleId());
        }

        if (!jpaPropertyContractRepository.existsById(contractPartyRequest.getContractId())){
            throw new PropertyContractNotFoundException(contractPartyRequest.getContractId());
        }

        ContractParty contractParty = contractPartyMapper.toEntity(contractPartyRequest);

        return contractPartyMapper.toResponse(jpaContractPartyRepository.save(contractParty));
    }
}
