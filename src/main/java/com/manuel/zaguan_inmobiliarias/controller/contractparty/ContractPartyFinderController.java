package com.manuel.zaguan_inmobiliarias.controller.contractparty;

import com.manuel.zaguan_inmobiliarias.dto.response.contractparty.ContractPartyResponse;
import com.manuel.zaguan_inmobiliarias.service.contractparty.ContractPartyFinderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contract_parties")
@AllArgsConstructor
public class ContractPartyFinderController {
    private final ContractPartyFinderService contractPartyFinderService;

    @GetMapping("/{id}")
    public ResponseEntity<ContractPartyResponse> findById(@PathVariable Long id){
        ContractPartyResponse contractPartyResponse = contractPartyFinderService.findById(id);

        return ResponseEntity.ok(contractPartyResponse);
    }

    @GetMapping
    public ResponseEntity<Page<ContractPartyResponse>> findAll(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ContractPartyResponse> contractPartyResponses = contractPartyFinderService.findAll(pageable);

        return ResponseEntity.ok(contractPartyResponses);
    }
}
