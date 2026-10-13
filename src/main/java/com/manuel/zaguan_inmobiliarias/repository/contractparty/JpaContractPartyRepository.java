package com.manuel.zaguan_inmobiliarias.repository.contractparty;

import com.manuel.zaguan_inmobiliarias.entity.contractparty.ContractParty;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaContractPartyRepository extends JpaRepository<ContractParty, Long> {
    boolean existsByIdAndRole(@NonNull Long id,@NonNull String role);
}
