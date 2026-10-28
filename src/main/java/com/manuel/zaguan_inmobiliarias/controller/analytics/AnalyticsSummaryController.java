package com.manuel.zaguan_inmobiliarias.controller.analytics;

import com.manuel.zaguan_inmobiliarias.dto.response.analytics.AnalyticsResponse;
import com.manuel.zaguan_inmobiliarias.service.analytics.AnalyticsFinderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@AllArgsConstructor
public class AnalyticsSummaryController {
    private final AnalyticsFinderService analyticsFinderService;

    //agencyId solo lo tiene en cuenta el admin
    @GetMapping("/summary")
    public ResponseEntity<AnalyticsResponse> summary(@RequestParam(required = false) Long agencyId){
        return ResponseEntity.ok(analyticsFinderService.summary(agencyId));
    }
}
