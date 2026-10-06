package com.manuel.zaguan_inmobiliarias.repository.crm;

import com.manuel.zaguan_inmobiliarias.entity.crm.CrmAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaCrmAlertRepository extends JpaRepository<CrmAlert, Long> {
    //Por fecha de la alerta: la mas proxima primero
    List<CrmAlert> findByCrmPropertyIdOrderByAlertDateAsc(Long crmPropertyId);

    //Por id y por lead: una alerta de otro lead no se ve ni se toca desde esta URL
    Optional<CrmAlert> findByIdAndCrmPropertyId(Long alertId, Long crmPropertyId);
}
