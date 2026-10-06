package com.manuel.zaguan_inmobiliarias.repository.crm;

import com.manuel.zaguan_inmobiliarias.entity.crm.CrmHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaCrmHistoryRepository extends JpaRepository<CrmHistory, Long> {
    //Los mas nuevos primero
    List<CrmHistory> findByCrmPropertyIdOrderByCreatedAtDesc(Long crmPropertyId);

    //Por id y por lead: un evento de otro lead no se ve desde esta URL
    Optional<CrmHistory> findByIdAndCrmPropertyId(Long historyId, Long crmPropertyId);
}
