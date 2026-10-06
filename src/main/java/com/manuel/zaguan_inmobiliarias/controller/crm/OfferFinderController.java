package com.manuel.zaguan_inmobiliarias.controller.crm;

import com.manuel.zaguan_inmobiliarias.dto.response.crm.OfferResponse;
import com.manuel.zaguan_inmobiliarias.service.crm.OfferFinderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/crm_properties/{crmPropertyId}/offers")
@AllArgsConstructor
public class OfferFinderController {
    private final OfferFinderService offerFinderService;

    @GetMapping
    public ResponseEntity<List<OfferResponse>> findByCrmProperty(@PathVariable Long crmPropertyId){
        return ResponseEntity.ok(offerFinderService.findByCrmProperty(crmPropertyId));
    }

    @GetMapping("/{offerId}")
    public ResponseEntity<OfferResponse> findById(@PathVariable Long crmPropertyId,
                                                  @PathVariable Long offerId){
        return ResponseEntity.ok(offerFinderService.findById(crmPropertyId, offerId));
    }
}
