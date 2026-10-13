package com.manuel.zaguan_inmobiliarias.repository.property.contract;

import com.manuel.zaguan_inmobiliarias.entity.property.contract.PropertyContract;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface JpaPropertyContractRepository extends JpaRepository<PropertyContract, Long> {
    Optional<PropertyContract> findByType(@NonNull String type);
    Optional<PropertyContract> findByStatus(@NonNull String status);
    Optional<PropertyContract> findByCurrency(@NonNull String currency);
    Optional<PropertyContract> findByDocumentURL(@NonNull String documentURl);
    Optional<PropertyContract> findByAmount(@NonNull BigDecimal amount);

    boolean existsByType(@NonNull String type);
    boolean existsByStatus(@NonNull String status);
    boolean existsByCurrency(@NonNull String currency);
    boolean existsByDocumentURL(@NonNull String documentURl);
    boolean existsByAmount(@NonNull BigDecimal amount);
}
