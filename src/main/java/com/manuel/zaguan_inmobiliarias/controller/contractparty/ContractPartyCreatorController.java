package com.manuel.zaguan_inmobiliarias.controller.contractparty;

import com.manuel.zaguan_inmobiliarias.dto.request.contractparty.ContractPartyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.contractparty.ContractPartyResponse;
import com.manuel.zaguan_inmobiliarias.service.contractparty.ContractPartyCreatorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contract_parties")
@AllArgsConstructor
public class ContractPartyCreatorController {
    private final ContractPartyCreatorService contractPartyCreatorService;

    @PostMapping
    public ResponseEntity<ContractPartyResponse> create(@Valid @RequestBody ContractPartyRequest contractPartyRequest){
        ContractPartyResponse contractPartyResponse = contractPartyCreatorService.create(contractPartyRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(contractPartyResponse);
    }
}
