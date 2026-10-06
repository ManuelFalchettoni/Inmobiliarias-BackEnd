package com.manuel.zaguan_inmobiliarias.controller.crm;

import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmHistoryResponse;
import com.manuel.zaguan_inmobiliarias.service.crm.CrmHistoryFinderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/crm_properties/{crmPropertyId}/history")
@AllArgsConstructor
public class CrmHistoryFinderController {
    private final CrmHistoryFinderService crmHistoryFinderService;

    @GetMapping
    public ResponseEntity<List<CrmHistoryResponse>> findByCrmProperty(@PathVariable Long crmPropertyId){
        return ResponseEntity.ok(crmHistoryFinderService.findByCrmProperty(crmPropertyId));
    }

    @GetMapping("/{historyId}")
    public ResponseEntity<CrmHistoryResponse> findById(@PathVariable Long crmPropertyId,
                                                       @PathVariable Long historyId){
        return ResponseEntity.ok(crmHistoryFinderService.findById(crmPropertyId, historyId));
    }
}
