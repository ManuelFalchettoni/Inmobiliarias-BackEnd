package com.manuel.zaguan_inmobiliarias.service.property;

import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.enums.crm.CrmStage;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractStatus;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyHasActiveContractsException;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyHasOpenLeadsException;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.contract.JpaPropertyContractRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PropertyDeleteService {
    private final JpaPropertyRepository jpaPropertyRepository;
    private final JpaPropertyContractRepository jpaPropertyContractRepository;
    private final JpaCrmPropertyRepository jpaCrmPropertyRepository;
    private final AgencyScopeService agencyScopeService;

    public PropertyDeleteService (JpaPropertyRepository jpaPropertyRepository,
                                  JpaPropertyContractRepository jpaPropertyContractRepository,
                                  JpaCrmPropertyRepository jpaCrmPropertyRepository,
                                  AgencyScopeService agencyScopeService){
        this.jpaPropertyRepository = jpaPropertyRepository;
        this.jpaPropertyContractRepository = jpaPropertyContractRepository;
        this.jpaCrmPropertyRepository = jpaCrmPropertyRepository;
        this.agencyScopeService = agencyScopeService;
    }

    @Transactional //Necesario porque no hay un metodo save para guardar el cambio
    public void delete(Long id){
        Property property = jpaPropertyRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new PropertyNotFoundException(id));

        if (!agencyScopeService.canAccess(property.getAgencyId())) {
            throw new PropertyNotFoundException(id);
        }

        //La baja es la misma que archivar: mismos controles que PropertyArchiveService
        if (jpaPropertyContractRepository.existsByPropertyIdAndStatus(id, ContractStatus.ACTIVE)) {
            throw new PropertyHasActiveContractsException(id);
        }
        if (jpaCrmPropertyRepository.existsByPropertyIdAndStageNotIn(id, List.of(CrmStage.CLOSED, CrmStage.DISCARDED))) {
            throw new PropertyHasOpenLeadsException(id);
        }

        property.setActive(false);
    }
}
