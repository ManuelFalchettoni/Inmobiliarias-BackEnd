package com.manuel.zaguan_inmobiliarias.service.contractparty;

import com.manuel.zaguan_inmobiliarias.dto.request.contractparty.ContractPartyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.contractparty.ContractPartyResponse;
import com.manuel.zaguan_inmobiliarias.entity.contractparty.ContractParty;
import com.manuel.zaguan_inmobiliarias.exception.contractparty.ContractPartyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.contractparty.ContractPartyMapper;
import com.manuel.zaguan_inmobiliarias.repository.contractparty.JpaContractPartyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContractPartyUpdaterService {
    private final JpaContractPartyRepository jpaContractPartyRepository;
    private final ContractPartyMapper contractPartyMapper;

    public ContractPartyResponse update(Long id, ContractPartyRequest contractPartyRequest){
        ContractParty toUpdate = jpaContractPartyRepository.findById(id)
                .orElseThrow(() -> new ContractPartyNotFoundException(id));

        toUpdate.setContractId(contractPartyRequest.getContractId());
        toUpdate.setPeopleId(contractPartyRequest.getPeopleId());
        toUpdate.setComments(contractPartyRequest.getComments());

        return contractPartyMapper.toResponse(jpaContractPartyRepository.save(toUpdate));
    }
}
