package com.manuel.zaguan_inmobiliarias.controller.contractparty;

import com.manuel.zaguan_inmobiliarias.dto.request.contractparty.ContractPartyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.contractparty.ContractPartyResponse;
import com.manuel.zaguan_inmobiliarias.service.contractparty.ContractPartyUpdaterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contract_parties")
@AllArgsConstructor
public class ContractPartyUpdaterController {
    private final ContractPartyUpdaterService contractPartyUpdaterService;

    @PutMapping("/{id}")
    public ResponseEntity<ContractPartyResponse> update(@PathVariable Long id, @Valid @RequestBody ContractPartyRequest contractPartyRequest){
        ContractPartyResponse contractPartyResponse = contractPartyUpdaterService.update(id, contractPartyRequest);

        return ResponseEntity.ok(contractPartyResponse);
    }
}
