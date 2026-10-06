package com.manuel.zaguan_inmobiliarias.repository.crm;

import com.manuel.zaguan_inmobiliarias.entity.crm.CrmProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCrmPropertyRepository extends JpaRepository<CrmProperty, Long> {
    //Los leads de un agente
    Page<CrmProperty> findAllByUserId(Long userId, Pageable pageable);
}
