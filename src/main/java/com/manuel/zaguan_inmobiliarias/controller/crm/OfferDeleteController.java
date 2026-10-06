package com.manuel.zaguan_inmobiliarias.controller.crm;

import com.manuel.zaguan_inmobiliarias.service.crm.OfferDeleterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crm_properties/{crmPropertyId}/offers")
@AllArgsConstructor
public class OfferDeleteController {
    private final OfferDeleterService offerDeleterService;

    @DeleteMapping("/{offerId}")
    public ResponseEntity<Void> delete(@PathVariable Long crmPropertyId,
                                       @PathVariable Long offerId){
        offerDeleterService.delete(crmPropertyId, offerId);
        return ResponseEntity.noContent().build();
    }
}
