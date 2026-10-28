package com.manuel.zaguan_inmobiliarias.repository.crm;

import com.manuel.zaguan_inmobiliarias.entity.crm.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaOfferRepository extends JpaRepository<Offer, Long> {
    //Las mas nuevas primero
    List<Offer> findByCrmPropertyIdOrderByCreatedAtDesc(Long crmPropertyId);

    //Por id y por lead: una oferta de otro lead no se ve ni se toca desde esta URL
    Optional<Offer> findByIdAndCrmPropertyId(Long offerId, Long crmPropertyId);

    //Para Analitica: todas las ofertas de la inmobiliaria, sin paginar. La oferta llega a la
    //inmobiliaria por su lead y el lead por su propiedad. agencyId en null (solo el admin) trae todas
    @Query("""
            SELECT o FROM Offer o
            WHERE (:agencyId IS NULL OR o.crmPropertyId IN
                      (SELECT l.id FROM CrmProperty l WHERE l.propertyId IN
                          (SELECT p.id FROM Property p WHERE p.agencyId = :agencyId)))
            """)
    List<Offer> findAllForAnalytics(@Param("agencyId") Long agencyId);
}
