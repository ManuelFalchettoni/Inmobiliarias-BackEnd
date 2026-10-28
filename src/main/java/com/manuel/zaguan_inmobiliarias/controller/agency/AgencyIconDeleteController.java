package com.manuel.zaguan_inmobiliarias.controller.agency;

import com.manuel.zaguan_inmobiliarias.service.agency.AgencyIconDeleteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/agencies/{id}/icon")
public class AgencyIconDeleteController {
    private final AgencyIconDeleteService agencyIconDeleteService;

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        agencyIconDeleteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
