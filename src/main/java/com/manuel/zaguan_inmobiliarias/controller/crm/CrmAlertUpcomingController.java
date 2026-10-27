package com.manuel.zaguan_inmobiliarias.controller.crm;

import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmAlertResponse;
import com.manuel.zaguan_inmobiliarias.service.crm.CrmAlertFinderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//Fuera de /crm_properties/{id}/alerts: junta las alertas de todos los leads de la inmobiliaria
@RestController
@RequestMapping("/api/crm_alerts")
@AllArgsConstructor
public class CrmAlertUpcomingController {
    private final CrmAlertFinderService crmAlertFinderService;

    @GetMapping("/upcoming")
    public ResponseEntity<List<CrmAlertResponse>> upcoming(
            @RequestParam(required = false) Integer days,
            @RequestParam(required = false) Long userId){
        return ResponseEntity.ok(crmAlertFinderService.findUpcoming(days, userId));
    }
}
