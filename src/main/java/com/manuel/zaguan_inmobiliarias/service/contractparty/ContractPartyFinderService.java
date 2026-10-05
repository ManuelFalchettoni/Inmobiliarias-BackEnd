package com.manuel.zaguan_inmobiliarias.service.contractparty;

import com.manuel.zaguan_inmobiliarias.dto.response.contractparty.ContractPartyResponse;
import com.manuel.zaguan_inmobiliarias.entity.contractparty.ContractParty;
import com.manuel.zaguan_inmobiliarias.exception.contractparty.ContractPartyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.contractparty.ContractPartyMapper;
import com.manuel.zaguan_inmobiliarias.repository.contractparty.JpaContractPartyRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContractPartyFinderService {
    private final JpaContractPartyRepository jpaContractPartyRepository;
    private final ContractPartyMapper contractPartyMapper;

    public ContractPartyResponse findById(Long id){
        ContractParty contractParty = jpaContractPartyRepository.findById(id)
                .orElseThrow( () -> new ContractPartyNotFoundException(id));

        return contractPartyMapper.toResponse(contractParty);
    }

    public Page<ContractPartyResponse> findAll(Pageable pageable){
        Page<ContractParty> contractParties = jpaContractPartyRepository.findAll(pageable);

        return  contractParties.map(
                contractPartyMapper :: toResponse
        );
    }
}
