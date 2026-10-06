package com.manuel.zaguan_inmobiliarias.mapper.contractparty;

import com.manuel.zaguan_inmobiliarias.dto.request.contractparty.ContractPartyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.contractparty.ContractPartyResponse;
import com.manuel.zaguan_inmobiliarias.entity.contractparty.ContractParty;
import org.springframework.stereotype.Component;

@Component
public class ContractPartyMapper {
    public ContractParty toEntity(ContractPartyRequest contractPartyRequest){
        ContractParty contractParty = new ContractParty();

        contractParty.setContractId(contractPartyRequest.getContractId());
        contractParty.setPeopleId(contractPartyRequest.getPeopleId());
        contractParty.setRole(contractPartyRequest.getRole());
        contractParty.setComments(contractPartyRequest.getComments());

        return contractParty;
    }

    public ContractPartyResponse toResponse(ContractParty contractParty){
        ContractPartyResponse contractPartyResponse = new ContractPartyResponse();

        contractPartyResponse.setId(contractParty.getId());
        contractPartyResponse.setContractId(contractParty.getContractId());
        contractPartyResponse.setPeopleId(contractParty.getPeopleId());
        contractPartyResponse.setRole(contractParty.getRole());
        contractPartyResponse.setComments(contractParty.getComments());

        return  contractPartyResponse;
    }
}
