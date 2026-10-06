package com.manuel.zaguan_inmobiliarias.service.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.OfferRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.OfferResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.Offer;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmPropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.crm.OfferNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.crm.OfferMapper;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaOfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OfferUpdaterService {
    private final JpaOfferRepository jpaOfferRepository;
    private final JpaCrmPropertyRepository jpaCrmPropertyRepository;
    private final OfferMapper offerMapper;

    @Transactional
    public OfferResponse update(Long crmPropertyId, Long offerId, OfferRequest request){
        if (!jpaCrmPropertyRepository.existsById(crmPropertyId)) {
            throw new CrmPropertyNotFoundException(crmPropertyId);
        }

        //Por id y por lead: una oferta de otro lead no se edita desde esta URL
        Offer offer = jpaOfferRepository.findByIdAndCrmPropertyId(offerId, crmPropertyId)
                .orElseThrow(() -> new OfferNotFoundException(offerId));

        offerMapper.updateEntity(request, offer);
        //updatedAt lo pone @UpdateTimestamp al flushear
        return offerMapper.toResponse(jpaOfferRepository.saveAndFlush(offer));
    }
}
