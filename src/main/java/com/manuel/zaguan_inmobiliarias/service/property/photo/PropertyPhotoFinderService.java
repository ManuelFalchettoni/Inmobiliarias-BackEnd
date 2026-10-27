package com.manuel.zaguan_inmobiliarias.service.property.photo;

import com.manuel.zaguan_inmobiliarias.dto.response.property.photo.PropertyPhotoResponse;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.photo.PropertyPhotoNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.property.photo.PropertyPhotoMapper;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import com.manuel.zaguan_inmobiliarias.repository.property.photo.JpaPropertyPhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PropertyPhotoFinderService {

    private final JpaPropertyPhotoRepository jpaPropertyPhotoRepository;
    private final AgencyScopeService agencyScopeService;
    private final PropertyPhotoMapper propertyPhotoMapper;

    public PropertyPhotoFinderService(JpaPropertyPhotoRepository jpaPropertyPhotoRepository,
                                      AgencyScopeService agencyScopeService,
                                      PropertyPhotoMapper propertyPhotoMapper) {
        this.jpaPropertyPhotoRepository = jpaPropertyPhotoRepository;
        this.agencyScopeService = agencyScopeService;
        this.propertyPhotoMapper = propertyPhotoMapper;
    }

    public List<PropertyPhotoResponse> findByProperty(Long propertyId) {
        requireProperty(propertyId);

        return jpaPropertyPhotoRepository.findByPropertyIdOrderByPositionAsc(propertyId)
                .stream()
                .map(propertyPhotoMapper::toResponse)
                .toList();
    }

    public PropertyPhotoResponse findById(Long propertyId, Long photoId) {
        requireProperty(propertyId);

        return jpaPropertyPhotoRepository.findByIdAndPropertyId(photoId, propertyId)
                .map(propertyPhotoMapper::toResponse)
                .orElseThrow(() -> new PropertyPhotoNotFoundException(photoId));
    }

    private void requireProperty(Long propertyId) {
        if (!agencyScopeService.canAccessActiveProperty(propertyId)) {
            throw new PropertyNotFoundException(propertyId);
        }
    }
}
