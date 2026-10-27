package com.manuel.zaguan_inmobiliarias.service.property.contract;

import com.manuel.zaguan_inmobiliarias.dto.response.property.contract.PropertyContractResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.contract.PropertyContract;
import com.manuel.zaguan_inmobiliarias.enums.Currency;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractStatus;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractType;
import com.manuel.zaguan_inmobiliarias.exception.property.contract.PropertyContractNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.contract.PropertyContractMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.contract.JpaPropertyContractRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class PropertyContractFinderService {
    private final JpaPropertyContractRepository jpaPropertyContractRepository;
    private final PropertyContractMapper propertyContractMapper;
    private final AgencyScopeService agencyScopeService;

    //Un contrato de otra inmobiliaria responde 404, igual que uno que no existe
    public PropertyContractResponse findById(Long id){
        PropertyContract propertyContract = jpaPropertyContractRepository.findById(id)
                .orElseThrow( () -> new PropertyContractNotFoundException(id));

        if (!agencyScopeService.canAccessProperty(propertyContract.getPropertyId())) {
            throw new PropertyContractNotFoundException(id);
        }
        return propertyContractMapper.toResponse(propertyContract);
    }

    //El listado es la busqueda sin filtros: asi la regla de la inmobiliaria esta en un solo lugar
    public Page<PropertyContractResponse> findAll(Pageable pageable){
        return search(null, null, null, null, null, null, null,
                null, null, null, null, pageable);
    }

    //agencyId lo respeta solo el admin; en null busca en todas
    public Page<PropertyContractResponse> search(Long agencyId, Long propertyId, ContractType type,
                                                 ContractStatus status, Currency currency,
                                                 BigDecimal minAmount, BigDecimal maxAmount,
                                                 LocalDate startFrom, LocalDate startTo,
                                                 LocalDate endFrom, LocalDate endTo, Pageable pageable){
        Long scopedAgencyId = agencyScopeService.resolveAgencyId(agencyId);
        Page<PropertyContract> propertyContracts = jpaPropertyContractRepository.search(scopedAgencyId, propertyId,
                type, status, currency, minAmount, maxAmount, startFrom, startTo, endFrom, endTo, pageable);

        return propertyContracts.map(
                propertyContractMapper :: toResponse
        );
    }
}
