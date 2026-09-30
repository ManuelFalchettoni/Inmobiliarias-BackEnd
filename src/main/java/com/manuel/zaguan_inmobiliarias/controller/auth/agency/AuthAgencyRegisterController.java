package com.manuel.zaguan_inmobiliarias.controller.auth.agency;

import com.manuel.zaguan_inmobiliarias.dto.request.auth.agency.AuthAgencyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.agency.AgencyResponse;
import com.manuel.zaguan_inmobiliarias.service.auth.agency.AuthAgencyRegisterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class AuthAgencyRegisterController {
    private final AuthAgencyRegisterService authAgencyRegisterService;

    @PostMapping
    public ResponseEntity<AgencyResponse> post(@Valid @RequestBody AuthAgencyRequest agencyRequest){

        AgencyResponse agencyResponse = authAgencyRegisterService.agencyRegister(agencyRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(agencyResponse);
    }
}
