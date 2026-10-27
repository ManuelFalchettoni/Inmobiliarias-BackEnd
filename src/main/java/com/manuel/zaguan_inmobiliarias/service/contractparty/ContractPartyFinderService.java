package com.manuel.zaguan_inmobiliarias.service.contractparty;

import com.manuel.zaguan_inmobiliarias.dto.response.contractparty.ContractPartyResponse;
import com.manuel.zaguan_inmobiliarias.entity.contractparty.ContractParty;
import com.manuel.zaguan_inmobiliarias.exception.contractparty.ContractPartyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.contractparty.ContractPartyMapper;
import com.manuel.zaguan_inmobiliarias.repository.contractparty.JpaContractPartyRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContractPartyFinderService {
    private final JpaContractPartyRepository jpaContractPartyRepository;
    private final ContractPartyMapper contractPartyMapper;
    private final AgencyScopeService agencyScopeService;

    //Una parte de otra inmobiliaria responde 404, igual que una que no existe
    public ContractPartyResponse findById(Long id){
        ContractParty contractParty = jpaContractPartyRepository.findById(id)
                .orElseThrow( () -> new ContractPartyNotFoundException(id));

        if (!agencyScopeService.canAccessContract(contractParty.getContractId())) {
            throw new ContractPartyNotFoundException(id);
        }
        return contractPartyMapper.toResponse(contractParty);
    }

    //Sin agencyId por parametro: el admin ve todas, una inmobiliaria las de sus contratos
    public Page<ContractPartyResponse> findAll(Pageable pageable){
        Long scopedAgencyId = agencyScopeService.resolveAgencyId(null);
        Page<ContractParty> contractParties = jpaContractPartyRepository.findAllByAgency(scopedAgencyId, pageable);

        return  contractParties.map(
                contractPartyMapper :: toResponse
        );
    }
}
