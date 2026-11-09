package com.manuel.zaguan_inmobiliarias.mapper.contractparty;

import com.manuel.zaguan_inmobiliarias.dto.request.contractparty.ContractPartyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.contractparty.ContractPartyResponse;
import com.manuel.zaguan_inmobiliarias.entity.contractparty.ContractParty;
import com.manuel.zaguan_inmobiliarias.entity.people.People;
import com.manuel.zaguan_inmobiliarias.mapper.people.PeopleMapper;
import com.manuel.zaguan_inmobiliarias.repository.people.JpaPeopleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class ContractPartyMapper {
    private final JpaPeopleRepository jpaPeopleRepository;
    private final PeopleMapper peopleMapper;

    public ContractParty toEntity(ContractPartyRequest contractPartyRequest){
        ContractParty contractParty = new ContractParty();

        contractParty.setContractId(contractPartyRequest.getContractId());
        contractParty.setPeopleId(contractPartyRequest.getPeopleId());
        contractParty.setRole(contractPartyRequest.getRole());
        contractParty.setComments(contractPartyRequest.getComments());

        return contractParty;
    }

    public ContractPartyResponse toResponse(ContractParty contractParty){
        return toResponses(List.of(contractParty)).get(0);
    }

    //Varias partes juntas: las personas se traen con una sola consulta (findAllById) en vez de una por parte
    public List<ContractPartyResponse> toResponses(List<ContractParty> contractParties){
        List<Long> peopleIds = contractParties.stream().map(ContractParty::getPeopleId).distinct().toList();

        Map<Long, People> peopleById = new HashMap<>();
        for (People people : jpaPeopleRepository.findAllById(peopleIds)) {
            peopleById.put(people.getId(), people);
        }

        List<ContractPartyResponse> responses = new ArrayList<>();
        for (ContractParty contractParty : contractParties) {
            ContractPartyResponse contractPartyResponse = new ContractPartyResponse();

            contractPartyResponse.setId(contractParty.getId());
            contractPartyResponse.setContractId(contractParty.getContractId());
            contractPartyResponse.setPeopleId(contractParty.getPeopleId());
            contractPartyResponse.setRole(contractParty.getRole());
            contractPartyResponse.setComments(contractParty.getComments());

            People people = peopleById.get(contractParty.getPeopleId());
            if (people != null) {
                contractPartyResponse.setPerson(peopleMapper.toSummary(people));
            }

            responses.add(contractPartyResponse);
        }
        return responses;
    }
}
