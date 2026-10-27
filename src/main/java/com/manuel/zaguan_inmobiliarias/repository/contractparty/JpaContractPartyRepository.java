package com.manuel.zaguan_inmobiliarias.repository.contractparty;

import com.manuel.zaguan_inmobiliarias.entity.contractparty.ContractParty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaContractPartyRepository extends JpaRepository<ContractParty, Long> {

    //La parte no tiene agencyId: es el de la propiedad de su contrato. agencyId en null (solo el
    //admin) trae todas
    @Query("""
            SELECT cp FROM ContractParty cp
            WHERE (:agencyId IS NULL OR cp.contractId IN
                      (SELECT c.id FROM PropertyContract c WHERE c.propertyId IN
                          (SELECT p.id FROM Property p WHERE p.agencyId = :agencyId)))
            """)
    Page<ContractParty> findAllByAgency(@Param("agencyId") Long agencyId, Pageable pageable);
}
