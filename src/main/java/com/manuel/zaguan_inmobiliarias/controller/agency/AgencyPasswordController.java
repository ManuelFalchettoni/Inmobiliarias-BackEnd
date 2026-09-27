package com.manuel.zaguan_inmobiliarias.controller.agency;

import com.manuel.zaguan_inmobiliarias.dto.request.agency.AgencyPasswordRequest;
import com.manuel.zaguan_inmobiliarias.service.agency.AgencyPasswordUpdaterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agencies")
@AllArgsConstructor
public class AgencyPasswordController {
    private final AgencyPasswordUpdaterService agencyPasswordUpdaterService;

    //La contraseña sale del PUT: ahi se editan los datos, aca se cambia la contraseña
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id,
                                               @Valid @RequestBody AgencyPasswordRequest agencyPasswordRequest){
        agencyPasswordUpdaterService.updatePassword(id, agencyPasswordRequest);
        return ResponseEntity.noContent().build();
    }
}
