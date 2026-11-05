package com.manuel.zaguan_inmobiliarias.controller.crm;

import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmAgendaResponse;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmAgendaSummaryResponse;
import com.manuel.zaguan_inmobiliarias.service.crm.CrmAgendaService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//La agenda: una fila por lead, paginada. Ejemplo, lo que vence en la proxima hora:
// /api/crm_alerts/agenda?bucket=hour
@RestController
@RequestMapping("/api/crm_alerts/agenda")
@AllArgsConstructor
public class CrmAgendaController {
    private final CrmAgendaService crmAgendaService;

    //bucket: overdue, hour, day, week o later. sort: proximo o lejano
    @GetMapping
    public ResponseEntity<Page<CrmAgendaResponse>> agenda(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "true") boolean includeOverdue,
            @RequestParam(defaultValue = "false") boolean includeWithoutAlerts,
            @RequestParam(required = false) String bucket,
            @RequestParam(defaultValue = "proximo") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size){
        return ResponseEntity.ok(crmAgendaService.findAgenda(userId, includeOverdue, includeWithoutAlerts,
                bucket, sort, page, size));
    }

    @GetMapping("/summary")
    public ResponseEntity<CrmAgendaSummaryResponse> summary(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "true") boolean includeOverdue){
        return ResponseEntity.ok(crmAgendaService.summary(userId, includeOverdue));
    }
}
