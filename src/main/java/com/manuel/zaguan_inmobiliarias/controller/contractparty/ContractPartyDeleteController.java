package com.manuel.zaguan_inmobiliarias.controller.contractparty;

import com.manuel.zaguan_inmobiliarias.service.contractparty.ContractPartyDeleterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contract_parties")
@AllArgsConstructor
public class ContractPartyDeleteController {
    private final ContractPartyDeleterService contractPartyDeleterService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        contractPartyDeleterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
