package com.manuel.zaguan_inmobiliarias.mapper.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.CrmPropertyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmPropertyResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmProperty;
import com.manuel.zaguan_inmobiliarias.mapper.property.PropertyMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CrmPropertyMapper {
    private final JpaPropertyRepository jpaPropertyRepository;
    private final PropertyMapper propertyMapper;

    public CrmProperty toEntity(CrmPropertyRequest request){
        CrmProperty crmProperty = new CrmProperty();
        updateEntity(request, crmProperty);
        return crmProperty;
    }

    public void updateEntity(CrmPropertyRequest request, CrmProperty crmProperty){
        crmProperty.setPropertyId(request.getPropertyId());
        crmProperty.setPeopleId(request.getPeopleId());
        crmProperty.setUserId(request.getUserId());
        crmProperty.setStage(request.getStage());
    }

    public CrmPropertyResponse toResponse(CrmProperty crmProperty){
        CrmPropertyResponse response = new CrmPropertyResponse();
        response.setId(crmProperty.getId());
        response.setPropertyId(crmProperty.getPropertyId());
        //findById y no findByIdAndActiveTrue: la propiedad archivada se sigue mostrando en sus leads
        response.setProperty(jpaPropertyRepository.findById(crmProperty.getPropertyId())
                .map(propertyMapper::toSummary)
                .orElse(null));
        response.setPeopleId(crmProperty.getPeopleId());
        response.setUserId(crmProperty.getUserId());
        response.setStage(crmProperty.getStage());
        response.setCreatedAt(crmProperty.getCreatedAt());
        response.setUpdatedAt(crmProperty.getUpdatedAt());
        return response;
    }
}
