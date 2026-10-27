package com.manuel.zaguan_inmobiliarias.repository.property.contract;

import com.manuel.zaguan_inmobiliarias.entity.property.contract.PropertyContract;
import com.manuel.zaguan_inmobiliarias.enums.Currency;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractStatus;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface JpaPropertyContractRepository extends JpaRepository<PropertyContract, Long> {

    //Busqueda con filtros opcionales, el que llega en null no filtra. agencyId en null es "todas":
    //solo le puede llegar asi al admin, el resto busca siempre en su inmobiliaria (AgencyScopeService).
    //El contrato no tiene agencyId, asi que se busca por la propiedad.
    //Las fechas son rangos: endFrom y endTo sirven para ver los contratos que vencen en un periodo
    @Query("""
            SELECT c FROM PropertyContract c
            WHERE (:agencyId IS NULL OR c.propertyId IN
                      (SELECT p.id FROM Property p WHERE p.agencyId = :agencyId))
              AND (:propertyId IS NULL OR c.propertyId = :propertyId)
              AND (:type IS NULL OR c.type = :type)
              AND (:status IS NULL OR c.status = :status)
              AND (:currency IS NULL OR c.currency = :currency)
              AND (:minAmount IS NULL OR c.amount >= :minAmount)
              AND (:maxAmount IS NULL OR c.amount <= :maxAmount)
              AND (:startFrom IS NULL OR c.startDate >= :startFrom)
              AND (:startTo IS NULL OR c.startDate <= :startTo)
              AND (:endFrom IS NULL OR c.endDate >= :endFrom)
              AND (:endTo IS NULL OR c.endDate <= :endTo)
            """)
    Page<PropertyContract> search(@Param("agencyId") Long agencyId,
                                  @Param("propertyId") Long propertyId,
                                  @Param("type") ContractType type,
                                  @Param("status") ContractStatus status,
                                  @Param("currency") Currency currency,
                                  @Param("minAmount") BigDecimal minAmount,
                                  @Param("maxAmount") BigDecimal maxAmount,
                                  @Param("startFrom") LocalDate startFrom,
                                  @Param("startTo") LocalDate startTo,
                                  @Param("endFrom") LocalDate endFrom,
                                  @Param("endTo") LocalDate endTo,
                                  Pageable pageable);
}
