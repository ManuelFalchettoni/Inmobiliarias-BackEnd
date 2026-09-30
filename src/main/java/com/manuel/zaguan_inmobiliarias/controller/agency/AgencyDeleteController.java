package com.manuel.zaguan_inmobiliarias.controller.agency;

import com.manuel.zaguan_inmobiliarias.config.UserPrincipal;
import com.manuel.zaguan_inmobiliarias.service.agency.AgencyDeleterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/agencies")
public class AgencyDeleteController {
    private final AgencyDeleterService agencyDeleterService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        agencyDeleterService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteAgency(@PathVariable Long id,
                                              @AuthenticationPrincipal UserPrincipal userPrincipal){
        agencyDeleterService.deleteAgency(id, userPrincipal.getId());

        return  ResponseEntity.noContent().build();
    }
}
