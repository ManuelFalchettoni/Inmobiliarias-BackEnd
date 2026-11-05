package com.manuel.zaguan_inmobiliarias.repository.crm;

import com.manuel.zaguan_inmobiliarias.entity.crm.CrmAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaCrmAlertRepository extends JpaRepository<CrmAlert, Long> {
    //Por fecha de la alerta: la mas proxima primero
    List<CrmAlert> findByCrmPropertyIdOrderByAlertDateAsc(Long crmPropertyId);

    //Por id y por lead: una alerta de otro lead no se ve ni se toca desde esta URL
    Optional<CrmAlert> findByIdAndCrmPropertyId(Long alertId, Long crmPropertyId);

    //Las pendientes hasta una fecha, vencidas incluidas: la mas proxima primero. until en null
    //trae todas, sin corte de fecha. Las DONE no salen.
    //La alerta no tiene agencyId: es el de la propiedad de su lead. agencyId en null (solo el
    //admin) trae todas; userId en null trae las de todos los agentes
    @Query("""
            SELECT a FROM CrmAlert a
            WHERE a.status = com.manuel.zaguan_inmobiliarias.enums.crm.CrmAlertStatus.PENDING
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

    //Las que estan por vencer y todavia no se avisaron. Las ya vencidas no salen: el primer
    //deploy no tiene que mandar un mail por cada alerta vieja
    @Query("""
            SELECT a FROM CrmAlert a
            WHERE a.status = com.manuel.zaguan_inmobiliarias.enums.crm.CrmAlertStatus.PENDING
              AND a.notifiedAt IS NULL
              AND a.alertDate > :now AND a.alertDate <= :until
            ORDER BY a.alertDate ASC
            """)
    List<CrmAlert> findToNotify(@Param("now") LocalDateTime now, @Param("until") LocalDateTime until);

    //UPDATE puntual y no save: no pisa una edicion hecha mientras se mandaba el mail
    @Transactional
    @Modifying
    @Query("UPDATE CrmAlert a SET a.notifiedAt = :notifiedAt WHERE a.id = :id")
    void markNotified(@Param("id") Long id, @Param("notifiedAt") LocalDateTime notifiedAt);
}
