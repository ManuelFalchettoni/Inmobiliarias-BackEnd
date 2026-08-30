package com.manuel.zaguan_inmobiliarias.controller.agency;

import com.manuel.zaguan_inmobiliarias.config.UserPrincipal;
import com.manuel.zaguan_inmobiliarias.dto.request.agency.AgencyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.service.agency.AgencyCreatorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agencies")
@AllArgsConstructor
public class AgencyCreatorController {
    private final AgencyCreatorService agencyCreatorService;

    @PostMapping
    public ResponseEntity<AgencyResponse> create(@Valid @RequestBody AgencyRequest agencyRequest,
                                                 @AuthenticationPrincipal UserPrincipal userPrincipal){
        AgencyResponse agencyResponse = agencyCreatorService.register(agencyRequest, userPrincipal.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(agencyResponse);
    }
}
