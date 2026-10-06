package com.manuel.zaguan_inmobiliarias.service.crm;

import com.manuel.zaguan_inmobiliarias.entity.crm.Offer;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmPropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.crm.OfferNotFoundException;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaOfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//Borrado fisico, como los precios. Una oferta rechazada no se borra: pasa a REJECTED con el PUT.
//El DELETE es para una cargada por error
@Service
@AllArgsConstructor
public class OfferDeleterService {
    private final JpaOfferRepository jpaOfferRepository;
    private final JpaCrmPropertyRepository jpaCrmPropertyRepository;

    @Transactional
    public void delete(Long crmPropertyId, Long offerId){
        if (!jpaCrmPropertyRepository.existsById(crmPropertyId)) {
            throw new CrmPropertyNotFoundException(crmPropertyId);
        }

        Offer offer = jpaOfferRepository.findByIdAndCrmPropertyId(offerId, crmPropertyId)
                .orElseThrow(() -> new OfferNotFoundException(offerId));

        jpaOfferRepository.delete(offer);
    }
}
