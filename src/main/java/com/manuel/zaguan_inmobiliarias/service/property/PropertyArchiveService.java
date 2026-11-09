package com.manuel.zaguan_inmobiliarias.service.property;

import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertyResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.enums.crm.CrmStage;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractStatus;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyHasActiveContractsException;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyHasOpenLeadsException;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.PropertyMapper;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.contract.JpaPropertyContractRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PropertyArchiveService {
    private final JpaPropertyRepository jpaPropertyRepository;
    private final JpaPropertyContractRepository jpaPropertyContractRepository;
    private final JpaCrmPropertyRepository jpaCrmPropertyRepository;
    private final PropertyMapper propertyMapper;
    private final AgencyScopeService agencyScopeService;

    public PropertyArchiveService (JpaPropertyRepository jpaPropertyRepository,
                                   JpaPropertyContractRepository jpaPropertyContractRepository,
                                   JpaCrmPropertyRepository jpaCrmPropertyRepository,
                                   PropertyMapper propertyMapper, AgencyScopeService agencyScopeService){
        this.jpaPropertyRepository = jpaPropertyRepository;
        this.jpaPropertyContractRepository = jpaPropertyContractRepository;
        this.jpaCrmPropertyRepository = jpaCrmPropertyRepository;
        this.propertyMapper = propertyMapper;
        this.agencyScopeService = agencyScopeService;
    }

    @Transactional
    public PropertyResponse archive(Long id){
        //findById pelado, igual que restore: si ya estaba archivada no hace nada
        //y devuelve 200 igual
        Property property = jpaPropertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException(id));

        if (!agencyScopeService.canAccess(property.getAgencyId())) {
            throw new PropertyNotFoundException(id);
        }

        //No se archiva con contratos vigentes ni leads abiertos: primero hay que terminarlos.
        //Si ya estaba archivada no se controla, para que repetir el pedido siga devolviendo 200
        if (property.getActive()) {
            if (jpaPropertyContractRepository.existsByPropertyIdAndStatus(id, ContractStatus.ACTIVE)) {
                throw new PropertyHasActiveContractsException(id);
            }
            if (jpaCrmPropertyRepository.existsByPropertyIdAndStageNotIn(id,
                    List.of(CrmStage.CLOSED, CrmStage.DISCARDED))) {
                throw new PropertyHasOpenLeadsException(id);
            }
        }

        property.setActive(false);

        //Flush para que updatedAt salga actualizado en la respuesta
        jpaPropertyRepository.saveAndFlush(property);
        return propertyMapper.toResponse(property);
    }
}
