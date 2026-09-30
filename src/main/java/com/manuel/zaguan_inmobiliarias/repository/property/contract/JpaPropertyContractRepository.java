package com.manuel.zaguan_inmobiliarias.repository.property.contract;

import com.manuel.zaguan_inmobiliarias.entity.property.contract.PropertyContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPropertyContractRepository extends JpaRepository<PropertyContract, Long> {
}
