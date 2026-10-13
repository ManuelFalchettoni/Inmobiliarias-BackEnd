package com.manuel.zaguan_inmobiliarias.controller.property.contract;

import com.manuel.zaguan_inmobiliarias.dto.response.property.contract.PropertyContractResponse;
import com.manuel.zaguan_inmobiliarias.enums.Currency;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractStatus;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractType;
import com.manuel.zaguan_inmobiliarias.service.property.contract.PropertyContractFinderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/api/property_contracts")
public class PropertyContractFinderController {
    private final PropertyContractFinderService propertyContractFinderService;

    @GetMapping("/{id}")
    public ResponseEntity<PropertyContractResponse> findById(@PathVariable Long id){
        PropertyContractResponse propertyContractResponse = propertyContractFinderService.findById(id);

        return ResponseEntity.ok(propertyContractResponse);
    }

    @GetMapping
    public ResponseEntity<Page<PropertyContractResponse>> findAll(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<PropertyContractResponse> propertyContractsResponse = propertyContractFinderService.findAll(pageable);

        return ResponseEntity.ok(propertyContractsResponse);
    }

    //agencyId obligatorio: cada inmobiliaria busca solo en sus contratos. El resto es opcional.
    //Las fechas van como 2026-12-31 y los enums con el nombre de la constante. Ejemplo, los que
    //vencen este año: /api/property_contracts/search?agencyId=1&endFrom=2026-01-01&endTo=2026-12-31
    @GetMapping("/search")
    public ResponseEntity<Page<PropertyContractResponse>> search(
            @RequestParam Long agencyId,
            @RequestParam(required = false) Long propertyId,
            @RequestParam(required = false) ContractType type,
            @RequestParam(required = false) ContractStatus status,
            @RequestParam(required = false) Currency currency,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startDate"));

        Page<PropertyContractResponse> propertyContractsResponse = propertyContractFinderService.search(agencyId,
                propertyId, type, status, currency, minAmount, maxAmount, startFrom, startTo, endFrom, endTo, pageable);

        return ResponseEntity.ok(propertyContractsResponse);
    }
}
