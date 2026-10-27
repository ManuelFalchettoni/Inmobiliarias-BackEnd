package com.manuel.zaguan_inmobiliarias.repository.crm;

import com.manuel.zaguan_inmobiliarias.entity.crm.CrmAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaCrmAlertRepository extends JpaRepository<CrmAlert, Long> {
    //Por fecha de la alerta: la mas proxima primero
    List<CrmAlert> findByCrmPropertyIdOrderByAlertDateAsc(Long crmPropertyId);

    //Por id y por lead: una alerta de otro lead no se ve ni se toca desde esta URL
    Optional<CrmAlert> findByIdAndCrmPropertyId(Long alertId, Long crmPropertyId);

    //Las no leidas hasta una fecha, vencidas incluidas: la mas proxima primero. until en null
    //trae todas, sin corte de fecha.
    //La alerta no tiene agencyId: es el de la propiedad de su lead. agencyId en null (solo el
    //admin) trae todas; userId en null trae las de todos los agentes
    @Query("""
            SELECT a FROM CrmAlert a
            WHERE a.isRead = false
              AND (:until IS NULL OR a.alertDate <= :until)
              AND (:userId IS NULL OR a.userId = :userId)
              AND (:agencyId IS NULL OR a.crmPropertyId IN
                      (SELECT l.id FROM CrmProperty l WHERE l.propertyId IN
                          (SELECT p.id FROM Property p WHERE p.agencyId = :agencyId)))
            ORDER BY a.alertDate ASC
            """)
    List<CrmAlert> findUpcoming(@Param("agencyId") Long agencyId,
                                @Param("userId") Long userId,
                                @Param("until") LocalDateTime until);
}
