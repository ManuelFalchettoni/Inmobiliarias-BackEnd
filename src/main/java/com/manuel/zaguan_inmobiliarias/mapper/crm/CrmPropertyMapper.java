package com.manuel.zaguan_inmobiliarias.mapper.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.CrmPropertyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmPropertyResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmProperty;
import org.springframework.stereotype.Component;

@Component
public class CrmPropertyMapper {

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
        response.setPeopleId(crmProperty.getPeopleId());
        response.setUserId(crmProperty.getUserId());
        response.setStage(crmProperty.getStage());
        response.setCreatedAt(crmProperty.getCreatedAt());
        response.setUpdatedAt(crmProperty.getUpdatedAt());
        return response;
    }
}
