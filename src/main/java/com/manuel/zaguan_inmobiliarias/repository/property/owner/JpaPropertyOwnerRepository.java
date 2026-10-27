package com.manuel.zaguan_inmobiliarias.repository.property.owner;

import com.manuel.zaguan_inmobiliarias.entity.property.Owner.PropertyOwner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPropertyOwnerRepository extends JpaRepository<PropertyOwner, Long>{
    boolean existsByPropertyIdAndPeopleId(Long propertyId, Long peopleId);

    //El vinculo no tiene agencyId: es el de su propiedad. agencyId en null (solo el admin) trae todos
    @Query("""
            SELECT o FROM PropertyOwner o
            WHERE (:agencyId IS NULL OR o.propertyId IN
                      (SELECT p.id FROM Property p WHERE p.agencyId = :agencyId))
            """)
    Page<PropertyOwner> findAllByAgency(@Param("agencyId") Long agencyId, Pageable pageable);
}
