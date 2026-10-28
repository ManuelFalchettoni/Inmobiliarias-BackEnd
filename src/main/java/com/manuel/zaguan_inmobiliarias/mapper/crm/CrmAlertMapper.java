package com.manuel.zaguan_inmobiliarias.mapper.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.CrmAlertRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmAlertResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmAlert;
import com.manuel.zaguan_inmobiliarias.enums.crm.CrmAlertStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CrmAlertMapper {

    //El crmPropertyId no se copia aca: sale de la URL y lo asigna CrmAlertCreatorService
    public CrmAlert toEntity(CrmAlertRequest request){
        CrmAlert alert = new CrmAlert();
        updateEntity(request, alert);
        return alert;
    }

    //El status no se toca: se cambia solo con el endpoint de completar
    public void updateEntity(CrmAlertRequest request, CrmAlert alert){
        alert.setUserId(request.getUserId());
        alert.setMessage(request.getMessage());
        alert.setAlertDate(request.getAlertDate());
    }

    public CrmAlertResponse toResponse(CrmAlert alert){
        CrmAlertResponse response = new CrmAlertResponse();
        response.setId(alert.getId());
        response.setCrmPropertyId(alert.getCrmPropertyId());
        response.setUserId(alert.getUserId());
        response.setMessage(alert.getMessage());
        response.setAlertDate(alert.getAlertDate());
        response.setStatus(alert.getStatus());
        response.setExpired(alert.getStatus() == CrmAlertStatus.PENDING
                && alert.getAlertDate().isBefore(LocalDateTime.now()));
        response.setCreatedAt(alert.getCreatedAt());
        return response;
    }
}
