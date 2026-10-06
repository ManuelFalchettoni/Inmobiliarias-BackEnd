package com.manuel.zaguan_inmobiliarias.repository.crm;

import com.manuel.zaguan_inmobiliarias.entity.crm.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaOfferRepository extends JpaRepository<Offer, Long> {
    //Las mas nuevas primero
    List<Offer> findByCrmPropertyIdOrderByCreatedAtDesc(Long crmPropertyId);

    //Por id y por lead: una oferta de otro lead no se ve ni se toca desde esta URL
    Optional<Offer> findByIdAndCrmPropertyId(Long offerId, Long crmPropertyId);
}
