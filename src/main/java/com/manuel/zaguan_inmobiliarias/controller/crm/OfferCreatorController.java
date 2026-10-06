package com.manuel.zaguan_inmobiliarias.controller.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.OfferRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.OfferResponse;
import com.manuel.zaguan_inmobiliarias.service.crm.OfferCreatorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crm_properties/{crmPropertyId}/offers")
@AllArgsConstructor
public class OfferCreatorController {
    private final OfferCreatorService offerCreatorService;

    @PostMapping
    public ResponseEntity<OfferResponse> create(@PathVariable Long crmPropertyId,
                                                @Valid @RequestBody OfferRequest request){
        OfferResponse response = offerCreatorService.create(crmPropertyId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
