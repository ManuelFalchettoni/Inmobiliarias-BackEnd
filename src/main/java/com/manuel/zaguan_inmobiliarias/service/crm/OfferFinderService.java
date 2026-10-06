package com.manuel.zaguan_inmobiliarias.service.crm;

import com.manuel.zaguan_inmobiliarias.dto.response.crm.OfferResponse;
import com.manuel.zaguan_inmobiliarias.exception.crm.CrmPropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.crm.OfferNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.crm.OfferMapper;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaOfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class OfferFinderService {
    private final JpaOfferRepository jpaOfferRepository;
    private final JpaCrmPropertyRepository jpaCrmPropertyRepository;
    private final OfferMapper offerMapper;

    //Sin paginar, como los precios: un lead tiene pocas ofertas
    public List<OfferResponse> findByCrmProperty(Long crmPropertyId){
        requireCrmProperty(crmPropertyId);
        return jpaOfferRepository.findByCrmPropertyIdOrderByCreatedAtDesc(crmPropertyId)
                .stream()
                .map(offerMapper::toResponse)
                .toList();
    }

    public OfferResponse findById(Long crmPropertyId, Long offerId){
        requireCrmProperty(crmPropertyId);
        return jpaOfferRepository.findByIdAndCrmPropertyId(offerId, crmPropertyId)
                .map(offerMapper::toResponse)
                .orElseThrow(() -> new OfferNotFoundException(offerId));
    }

    private void requireCrmProperty(Long crmPropertyId){
        if (!jpaCrmPropertyRepository.existsById(crmPropertyId)) {
            throw new CrmPropertyNotFoundException(crmPropertyId);
        }
    }
}
