package com.manuel.zaguan_inmobiliarias.controller.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.OfferRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.OfferResponse;
import com.manuel.zaguan_inmobiliarias.service.crm.OfferUpdaterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crm_properties/{crmPropertyId}/offers")
@AllArgsConstructor
public class OfferUpdaterController {
    private final OfferUpdaterService offerUpdaterService;

    @PutMapping("/{offerId}")
    public ResponseEntity<OfferResponse> update(@PathVariable Long crmPropertyId,
                                                @PathVariable Long offerId,
                                                @Valid @RequestBody OfferRequest request){
        return ResponseEntity.ok(offerUpdaterService.update(crmPropertyId, offerId, request));
    }
}
