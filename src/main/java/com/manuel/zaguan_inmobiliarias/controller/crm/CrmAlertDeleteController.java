package com.manuel.zaguan_inmobiliarias.controller.crm;

import com.manuel.zaguan_inmobiliarias.service.crm.CrmAlertDeleterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crm_properties/{crmPropertyId}/alerts")
@AllArgsConstructor
public class CrmAlertDeleteController {
    private final CrmAlertDeleterService crmAlertDeleterService;

    @DeleteMapping("/{alertId}")
    public ResponseEntity<Void> delete(@PathVariable Long crmPropertyId,
                                       @PathVariable Long alertId){
        crmAlertDeleterService.delete(crmPropertyId, alertId);
        return ResponseEntity.noContent().build();
    }
}
