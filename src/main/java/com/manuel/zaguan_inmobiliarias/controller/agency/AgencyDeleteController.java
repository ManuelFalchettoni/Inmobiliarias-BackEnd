package com.manuel.zaguan_inmobiliarias.controller.agency;

import com.manuel.zaguan_inmobiliarias.service.agency.AgencyDeleterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
