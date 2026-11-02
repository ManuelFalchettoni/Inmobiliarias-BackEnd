package com.manuel.zaguan_inmobiliarias.service.property;

import com.manuel.zaguan_inmobiliarias.dto.request.property.PropertySearchFilter;
import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertyResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.exception.property.InvalidSearchFilterException;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.PropertyMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.PropertySpecifications;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PropertyFinderService {

    private final JpaPropertyRepository jpaPropertyRepository;
    private final PropertyMapper propertyMapper;
    private final AgencyScopeService agencyScopeService;

    public PropertyFinderService (JpaPropertyRepository jpaPropertyRepository, PropertyMapper propertyMapper,
                                  AgencyScopeService agencyScopeService){
        this.jpaPropertyRepository = jpaPropertyRepository;
        this.propertyMapper = propertyMapper;
        this.agencyScopeService = agencyScopeService;
    }

    //Una de otra inmobiliaria responde 404, igual que una que no existe
    public PropertyResponse findById(Long id) {
        Property property = jpaPropertyRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new PropertyNotFoundException(id));

        if (!agencyScopeService.canAccess(property.getAgencyId())) {
            throw new PropertyNotFoundException(id);
        }
        return propertyMapper.toResponse(property);
    }

    //agencyId lo respeta solo el admin; en null lista todas
    public Page<PropertyResponse> findAll(Long agencyId, Boolean active, Pageable pageable) {
        Long scopedAgencyId = agencyScopeService.resolveAgencyId(agencyId);

        Page<Property> properties = (scopedAgencyId == null)
                ? jpaPropertyRepository.findAllByActive(active, pageable)
                : jpaPropertyRepository.findAllByAgencyIdAndActive(scopedAgencyId, active, pageable);
        return properties.map(propertyMapper::toResponse);
    }

    //sort=price ordena por el precio de la operacion y moneda del filtro, por eso exige las dos
    public Page<PropertyResponse> search(PropertySearchFilter filter, Pageable pageable) {
        validate(filter, pageable);

        Long scopedAgencyId = agencyScopeService.resolveAgencyId(filter.getAgencyId());
        Specification<Property> spec = PropertySpecifications.search(filter, scopedAgencyId);

        Page<Property> properties;
        if (pageable.getSort().getOrderFor(PropertySpecifications.PRICE_SORT) == null) {
            properties = jpaPropertyRepository.findAll(spec, pageable);
        } else {
            //El orden lo arma la Specification; el Pageable va sin sort para que Spring no lo pise.
            //El count usa la spec sin el join
            Pageable unsorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
            properties = jpaPropertyRepository.findAll(
                    PropertySpecifications.sortedByPrice(spec, filter, pageable.getSort()), spec, unsorted);
        }
        return properties.map(propertyMapper::toResponse);
    }

    private void validate(PropertySearchFilter filter, Pageable pageable) {
        checkRange("Rooms", filter.getMinRooms(), filter.getMaxRooms());
        checkRange("Size", filter.getMinSize(), filter.getMaxSize());
        checkRange("Year", filter.getMinYear(), filter.getMaxYear());
        checkRange("Floor", filter.getMinFloor(), filter.getMaxFloor());
        checkRange("Lat", filter.getMinLat(), filter.getMaxLat());
        checkRange("Lng", filter.getMinLng(), filter.getMaxLng());
        checkRange("Price", filter.getMinPrice(), filter.getMaxPrice());

        //Sin moneda el rango mezclaria pesos con dolares
        if ((filter.getMinPrice() != null || filter.getMaxPrice() != null) && filter.getCurrency() == null) {
            throw new InvalidSearchFilterException("currency is required when filtering by price");
        }

        boolean sortByPrice = pageable.getSort().getOrderFor(PropertySpecifications.PRICE_SORT) != null;
        if (sortByPrice && (filter.getOperationType() == null || filter.getCurrency() == null)) {
            throw new InvalidSearchFilterException("operationType and currency are required to sort by price");
        }
    }

    private <T extends Comparable<? super T>> void checkRange(String field, T min, T max) {
        if (min != null && max != null && min.compareTo(max) > 0) {
            throw new InvalidSearchFilterException("min" + field + " cannot be greater than max" + field);
        }
    }
}
