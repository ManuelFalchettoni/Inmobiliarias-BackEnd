package com.manuel.zaguan_inmobiliarias.service.property;

import com.manuel.zaguan_inmobiliarias.dto.response.property.PropertyResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.enums.Currency;
import com.manuel.zaguan_inmobiliarias.enums.property.OperationType;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyCondition;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyOccupancy;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyType;
import com.manuel.zaguan_inmobiliarias.exception.property.InvalidSearchFilterException;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.PropertyMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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

    public Page<PropertyResponse> search(Long agencyId, PropertyType type, String province, String county,
                                         String city, Integer minRooms, Integer maxRooms,
                                         PropertyCondition condition, PropertyOccupancy occupancy,
                                         Integer minSize, Integer maxSize, Integer minYear, Integer maxYear,
                                         OperationType operationType, Currency currency,
                                         BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        checkRange("rooms", minRooms, maxRooms);
        checkRange("size", minSize, maxSize);
        checkRange("year", minYear, maxYear);
        checkRange("price", minPrice, maxPrice);

        //Sin moneda el rango mezclaria pesos con dolares
        if ((minPrice != null || maxPrice != null) && currency == null) {
            throw new InvalidSearchFilterException("currency is required when filtering by price");
        }

        Long scopedAgencyId = agencyScopeService.resolveAgencyId(agencyId);

        return jpaPropertyRepository.search(scopedAgencyId, type, province, county, city, minRooms, maxRooms,
                        condition, occupancy, minSize, maxSize, minYear, maxYear, operationType, currency,
                        minPrice, maxPrice, pageable)
                .map(propertyMapper::toResponse);
    }

    private <T extends Comparable<T>> void checkRange(String field, T min, T max) {
        if (min != null && max != null && min.compareTo(max) > 0) {
            throw new InvalidSearchFilterException("min" + capitalize(field) + " cannot be greater than max"
                    + capitalize(field));
        }
    }

    private String capitalize(String field) {
        return Character.toUpperCase(field.charAt(0)) + field.substring(1);
    }
}
