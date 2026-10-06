package com.manuel.zaguan_inmobiliarias.mapper.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.CrmHistoryRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmHistoryResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmHistory;
import org.springframework.stereotype.Component;

@Component
public class CrmHistoryMapper {

    //El crmPropertyId no se copia aca: sale de la URL y lo asigna CrmHistoryCreatorService
    public CrmHistory toEntity(CrmHistoryRequest request){
        CrmHistory history = new CrmHistory();
        history.setUserId(request.getUserId());
        history.setType(request.getType());
        history.setComments(request.getComments());
        return history;
    }

    public CrmHistoryResponse toResponse(CrmHistory history){
        CrmHistoryResponse response = new CrmHistoryResponse();
        response.setId(history.getId());
        response.setCrmPropertyId(history.getCrmPropertyId());
        response.setUserId(history.getUserId());
        response.setType(history.getType());
        response.setComments(history.getComments());
        response.setCreatedAt(history.getCreatedAt());
        return response;
    }
}
