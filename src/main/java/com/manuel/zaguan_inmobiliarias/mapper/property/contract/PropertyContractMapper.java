package com.manuel.zaguan_inmobiliarias.mapper.property.contract;

import com.manuel.zaguan_inmobiliarias.dto.request.property.contract.PropertyContractRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.property.contract.PropertyContractResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.contract.PropertyContract;
import com.manuel.zaguan_inmobiliarias.mapper.property.PropertyMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PropertyContractMapper {
    private final JpaPropertyRepository jpaPropertyRepository;
    private final PropertyMapper propertyMapper;

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
        PropertyContractResponse propertyContractResponse = new PropertyContractResponse();

        propertyContractResponse.setId(propertyContract.getId());
        propertyContractResponse.setPropertyId(propertyContract.getPropertyId());
        //findById y no findByIdAndActiveTrue: la propiedad archivada se sigue mostrando en sus contratos
        propertyContractResponse.setProperty(jpaPropertyRepository.findById(propertyContract.getPropertyId())
                .map(propertyMapper::toSummary)
                .orElse(null));
        propertyContractResponse.setType(propertyContract.getType());
        propertyContractResponse.setStatus(propertyContract.getStatus());
        propertyContractResponse.setAmount(propertyContract.getAmount());
        propertyContractResponse.setCurrency(propertyContract.getCurrency());
        propertyContractResponse.setStartDate(propertyContract.getStartDate());
        propertyContractResponse.setEndDate(propertyContract.getEndDate());
        propertyContractResponse.setDocumentURL(propertyContract.getDocumentURL());
        propertyContractResponse.setCreatedAt(propertyContract.getCreatedAt());
        propertyContractResponse.setUpdatedAt(propertyContract.getUpdatedAt());

        return propertyContractResponse;
    }
}
