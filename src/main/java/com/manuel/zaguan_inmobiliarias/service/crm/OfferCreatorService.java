package com.manuel.zaguan_inmobiliarias.service.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.crm.OfferRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.OfferResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.Offer;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmPropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.crm.OfferMapper;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaOfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OfferCreatorService {
    private final JpaOfferRepository jpaOfferRepository;
    private final JpaCrmPropertyRepository jpaCrmPropertyRepository;
    private final OfferMapper offerMapper;

    @Transactional
    public OfferResponse create(Long crmPropertyId, OfferRequest request){
        //crmPropertyId es un id suelto, sin FK: se controla aca que el lead exista
        if (!jpaCrmPropertyRepository.existsById(crmPropertyId)) {
            throw new CrmPropertyNotFoundException(crmPropertyId);
        }

        Offer offer = offerMapper.toEntity(request);
        offer.setCrmPropertyId(crmPropertyId);
        return offerMapper.toResponse(jpaOfferRepository.save(offer));
    }
}
