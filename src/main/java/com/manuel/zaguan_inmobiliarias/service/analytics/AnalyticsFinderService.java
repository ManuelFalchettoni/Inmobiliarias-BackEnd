package com.manuel.zaguan_inmobiliarias.service.analytics;

import com.manuel.zaguan_inmobiliarias.dto.response.analytics.AgentAnalyticsResponse;
import com.manuel.zaguan_inmobiliarias.dto.response.analytics.AnalyticsResponse;
import com.manuel.zaguan_inmobiliarias.dto.response.analytics.ContractAnalyticsResponse;
import com.manuel.zaguan_inmobiliarias.dto.response.analytics.CrmAnalyticsResponse;
import com.manuel.zaguan_inmobiliarias.dto.response.analytics.PropertyAnalyticsResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmAlert;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmProperty;
import com.manuel.zaguan_inmobiliarias.entity.crm.Offer;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.entity.property.contract.PropertyContract;
import com.manuel.zaguan_inmobiliarias.entity.property.price.PropertyPrice;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.enums.Currency;
import com.manuel.zaguan_inmobiliarias.enums.crm.CrmStage;
import com.manuel.zaguan_inmobiliarias.enums.crm.OfferStatus;
import com.manuel.zaguan_inmobiliarias.enums.property.OperationType;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyType;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractStatus;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractType;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmAlertRepository;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaOfferRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.contract.JpaPropertyContractRepository;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//Trae las listas de la inmobiliaria y cuenta en Java, con loops: son pocos registros por
//inmobiliaria y se lee mas facil que un GROUP BY.
//Los Map arrancan con todas las constantes del enum en 0: el front recibe siempre las mismas claves
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class AnalyticsFinderService {
    private final JpaPropertyRepository jpaPropertyRepository;
    private final JpaCrmPropertyRepository jpaCrmPropertyRepository;
    private final JpaOfferRepository jpaOfferRepository;
    private final JpaCrmAlertRepository jpaCrmAlertRepository;
    private final JpaPropertyContractRepository jpaPropertyContractRepository;
    private final JpaUserRepository jpaUserRepository;
    private final AgencyScopeService agencyScopeService;

    //agencyId solo lo usa el admin; sin agencyId ve todas. El resto ve siempre la suya
    public AnalyticsResponse summary(Long agencyId){
        Long scopedAgencyId = agencyScopeService.resolveAgencyId(agencyId);

        List<CrmProperty> leads = jpaCrmPropertyRepository.findAllForAnalytics(scopedAgencyId);
        //Las pendientes, vencidas incluidas: la misma consulta que /crm_alerts/upcoming sin corte de fecha
        List<CrmAlert> pendingAlerts = jpaCrmAlertRepository.findUpcoming(scopedAgencyId, null, null);

        AnalyticsResponse response = new AnalyticsResponse();
        response.setProperties(properties(scopedAgencyId));
        response.setCrm(crm(scopedAgencyId, leads, pendingAlerts));
        response.setContracts(contracts(scopedAgencyId));
        response.setAgents(agents(leads, pendingAlerts));
        return response;
    }

    private PropertyAnalyticsResponse properties(Long agencyId){
        Map<PropertyType, Long> byType = new EnumMap<>(PropertyType.class);
        for (PropertyType type : PropertyType.values()) {
            byType.put(type, 0L);
        }
        Map<OperationType, Long> byOperation = new EnumMap<>(OperationType.class);
        for (OperationType operation : OperationType.values()) {
            byOperation.put(operation, 0L);
        }

        long active = 0;
        long inactive = 0;
        for (Property property : jpaPropertyRepository.findAllForAnalytics(agencyId)) {
            if (!property.getActive()) {
                inactive++;
                continue;
            }
            active++;
            byType.put(property.getType(), byType.get(property.getType()) + 1);

            //Un Set por si una propiedad tuviera dos precios de la misma operacion
            Set<OperationType> operations = new HashSet<>();
            for (PropertyPrice price : property.getPrices()) {
                operations.add(price.getOperationType());
            }
            for (OperationType operation : operations) {
                byOperation.put(operation, byOperation.get(operation) + 1);
            }
        }

        return new PropertyAnalyticsResponse(active, inactive, byType, byOperation);
    }

    private CrmAnalyticsResponse crm(Long agencyId, List<CrmProperty> leads, List<CrmAlert> pendingAlerts){
        Map<CrmStage, Long> leadsByStage = new EnumMap<>(CrmStage.class);
        for (CrmStage stage : CrmStage.values()) {
            leadsByStage.put(stage, 0L);
        }
        for (CrmProperty lead : leads) {
            leadsByStage.put(lead.getStage(), leadsByStage.get(lead.getStage()) + 1);
        }

        long closed = leadsByStage.get(CrmStage.CLOSED);
        long finished = closed + leadsByStage.get(CrmStage.DISCARDED);
        double closeRate = 0;
        if (finished > 0) {
            //Con un decimal: 66.7 y no 66.66666
            closeRate = Math.round(closed * 1000.0 / finished) / 10.0;
        }

        Map<OfferStatus, Long> offersByStatus = new EnumMap<>(OfferStatus.class);
        for (OfferStatus status : OfferStatus.values()) {
            offersByStatus.put(status, 0L);
        }
        for (Offer offer : jpaOfferRepository.findAllForAnalytics(agencyId)) {
            offersByStatus.put(offer.getStatus(), offersByStatus.get(offer.getStatus()) + 1);
        }

        long expired = 0;
        for (CrmAlert alert : pendingAlerts) {
            if (isExpired(alert)) {
                expired++;
            }
        }

        return new CrmAnalyticsResponse((long) leads.size(), leadsByStage, closeRate, offersByStatus,
                (long) pendingAlerts.size(), expired);
    }

    private ContractAnalyticsResponse contracts(Long agencyId){
        Map<ContractStatus, Long> byStatus = new EnumMap<>(ContractStatus.class);
        for (ContractStatus status : ContractStatus.values()) {
            byStatus.put(status, 0L);
        }
        Map<ContractType, Long> byType = new EnumMap<>(ContractType.class);
        for (ContractType type : ContractType.values()) {
            byType.put(type, 0L);
        }
        Map<Currency, BigDecimal> activeAmountByCurrency = new EnumMap<>(Currency.class);
        for (Currency currency : Currency.values()) {
            activeAmountByCurrency.put(currency, BigDecimal.ZERO);
        }

        List<PropertyContract> contracts = jpaPropertyContractRepository.findAllForAnalytics(agencyId);
        for (PropertyContract contract : contracts) {
            byStatus.put(contract.getStatus(), byStatus.get(contract.getStatus()) + 1);
            byType.put(contract.getType(), byType.get(contract.getType()) + 1);
            if (contract.getStatus() == ContractStatus.ACTIVE) {
                BigDecimal sum = activeAmountByCurrency.get(contract.getCurrency()).add(contract.getAmount());
                activeAmountByCurrency.put(contract.getCurrency(), sum);
            }
        }

        return new ContractAnalyticsResponse((long) contracts.size(), byStatus, byType, activeAmountByCurrency);
    }

    //Un renglon por cada agente que tenga leads o alertas pendientes. Los que no tienen nada no salen
    private List<AgentAnalyticsResponse> agents(List<CrmProperty> leads, List<CrmAlert> pendingAlerts){
        //LinkedHashMap: por userId, en el orden en que van apareciendo
        Map<Long, AgentAnalyticsResponse> byUser = new LinkedHashMap<>();

        for (CrmProperty lead : leads) {
            AgentAnalyticsResponse agent = agentRow(byUser, lead.getUserId());
            agent.setLeads(agent.getLeads() + 1);
            if (lead.getStage() == CrmStage.CLOSED) {
                agent.setClosedLeads(agent.getClosedLeads() + 1);
            }
        }
        for (CrmAlert alert : pendingAlerts) {
            AgentAnalyticsResponse agent = agentRow(byUser, alert.getUserId());
            if (isExpired(alert)) {
                agent.setExpiredAlerts(agent.getExpiredAlerts() + 1);
            }
        }

        //Los nombres en una sola consulta, no una por agente
        for (User user : jpaUserRepository.findAllById(byUser.keySet())) {
            byUser.get(user.getId()).setName(user.getName());
        }

        return new ArrayList<>(byUser.values());
    }

    private AgentAnalyticsResponse agentRow(Map<Long, AgentAnalyticsResponse> byUser, Long userId){
        AgentAnalyticsResponse agent = byUser.get(userId);
        if (agent == null) {
            agent = new AgentAnalyticsResponse(userId, null, 0L, 0L, 0L);
            byUser.put(userId, agent);
        }
        return agent;
    }

    //Igual que el expired de CrmAlertMapper: las que llegan aca ya son todas PENDING
    private boolean isExpired(CrmAlert alert){
        return alert.getAlertDate().isBefore(LocalDateTime.now());
    }
}
