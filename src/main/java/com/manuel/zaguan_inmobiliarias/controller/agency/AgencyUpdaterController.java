package com.manuel.zaguan_inmobiliarias.controller.agency;

import com.manuel.zaguan_inmobiliarias.config.UserPrincipal;
import com.manuel.zaguan_inmobiliarias.dto.request.agency.AgencyUpdaterRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.service.agency.AgencyUpdaterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/agencies")
public class AgencyUpdaterController {
    private final AgencyUpdaterService agencyUpdaterService;

    @PutMapping("/{id}")
    public ResponseEntity<AgencyResponse> put (@PathVariable Long id,
                                               @Valid @RequestBody AgencyUpdaterRequest agencyRequest,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal){
       AgencyResponse agencyResponse = agencyUpdaterService.update(id, agencyRequest, userPrincipal.getId());

       return ResponseEntity.ok(agencyResponse);
    }
}
