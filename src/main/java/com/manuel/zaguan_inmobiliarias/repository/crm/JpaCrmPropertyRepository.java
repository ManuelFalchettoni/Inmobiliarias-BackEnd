package com.manuel.zaguan_inmobiliarias.repository.crm;

import com.manuel.zaguan_inmobiliarias.entity.crm.CrmProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCrmPropertyRepository extends JpaRepository<CrmProperty, Long> {
    //Los leads de un agente
    Page<CrmProperty> findAllByUserId(Long userId, Pageable pageable);

    //El lead no tiene agencyId: es el de su propiedad. agencyId en null (solo el admin) trae todos
    @Query("""
            SELECT l FROM CrmProperty l
            WHERE (:agencyId IS NULL OR l.propertyId IN
                      (SELECT p.id FROM Property p WHERE p.agencyId = :agencyId))
            """)
    Page<CrmProperty> findAllByAgency(@Param("agencyId") Long agencyId, Pageable pageable);
}
