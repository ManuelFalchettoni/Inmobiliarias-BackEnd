package com.manuel.zaguan_inmobiliarias.mapper.property.contract;

import com.manuel.zaguan_inmobiliarias.dto.request.property.contract.PropertyContractRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.contractparty.ContractPartyResponse;
import com.manuel.zaguan_inmobiliarias.dto.response.property.contract.PropertyContractResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.entity.property.contract.PropertyContract;
import com.manuel.zaguan_inmobiliarias.mapper.contractparty.ContractPartyMapper;
import com.manuel.zaguan_inmobiliarias.mapper.property.PropertyMapper;
import com.manuel.zaguan_inmobiliarias.repository.contractparty.JpaContractPartyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class PropertyContractMapper {
    private final JpaPropertyRepository jpaPropertyRepository;
    private final JpaContractPartyRepository jpaContractPartyRepository;
    private final PropertyMapper propertyMapper;
    private final ContractPartyMapper contractPartyMapper;

    public PropertyContract toEntity(PropertyContractRequest propertyContractRequest){
        PropertyContract propertyContract = new PropertyContract();

        propertyContract.setType(propertyContractRequest.getType());
        propertyContract.setStatus(propertyContractRequest.getStatus());
        propertyContract.setAmount(propertyContractRequest.getAmount());
        propertyContract.setCurrency(propertyContractRequest.getCurrency());
        propertyContract.setStartDate(propertyContractRequest.getStartDate());
        propertyContract.setEndDate(propertyContractRequest.getEndDate());
        propertyContract.setDocumentURL(propertyContractRequest.getDocumentURL());
        propertyContract.setPropertyId(propertyContractRequest.getPropertyId());

        return propertyContract;
    }

    public PropertyContractResponse toResponse(PropertyContract propertyContract){
        return toResponses(List.of(propertyContract)).get(0);
    }

    //Varios contratos juntos, con su propiedad y sus partes (cada una con su persona). Todo se trae
    //con una consulta por tabla para la pagina entera, en vez de una por contrato
    public List<PropertyContractResponse> toResponses(List<PropertyContract> propertyContracts){
        //Pagina vacia: no hay nada que buscar, y asi no se arma un IN con la lista vacia
        if (propertyContracts.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> propertyIds = propertyContracts.stream().map(PropertyContract::getPropertyId).distinct().toList();
        List<Long> contractIds = propertyContracts.stream().map(PropertyContract::getId).toList();

        //findAllById y no el de activas: la propiedad archivada se sigue mostrando en sus contratos
        Map<Long, Property> propertyById = new HashMap<>();
        for (Property property : jpaPropertyRepository.findAllById(propertyIds)) {
            propertyById.put(property.getId(), property);
        }

        //Las partes agrupadas por contrato. El mapper de partes trae las personas de todas juntas
        Map<Long, List<ContractPartyResponse>> partiesByContract = new HashMap<>();
        for (Long contractId : contractIds) {
            partiesByContract.put(contractId, new ArrayList<>());
        }
        List<ContractPartyResponse> parties = contractPartyMapper.toResponses(
                jpaContractPartyRepository.findAllByContractIdIn(contractIds));
        for (ContractPartyResponse party : parties) {
            partiesByContract.get(party.getContractId()).add(party);
        }

        List<PropertyContractResponse> responses = new ArrayList<>();
        for (PropertyContract propertyContract : propertyContracts) {
            PropertyContractResponse propertyContractResponse = new PropertyContractResponse();

            propertyContractResponse.setId(propertyContract.getId());
            propertyContractResponse.setPropertyId(propertyContract.getPropertyId());
            Property property = propertyById.get(propertyContract.getPropertyId());
            if (property != null) {
                propertyContractResponse.setProperty(propertyMapper.toSummary(property));
            }
            propertyContractResponse.setParties(partiesByContract.get(propertyContract.getId()));
            propertyContractResponse.setType(propertyContract.getType());
            propertyContractResponse.setStatus(propertyContract.getStatus());
            propertyContractResponse.setAmount(propertyContract.getAmount());
            propertyContractResponse.setCurrency(propertyContract.getCurrency());
            propertyContractResponse.setStartDate(propertyContract.getStartDate());
            propertyContractResponse.setEndDate(propertyContract.getEndDate());
            propertyContractResponse.setDocumentURL(propertyContract.getDocumentURL());
            propertyContractResponse.setCreatedAt(propertyContract.getCreatedAt());
            propertyContractResponse.setUpdatedAt(propertyContract.getUpdatedAt());

            responses.add(propertyContractResponse);
        }
        return responses;
    }
}
