package com.manuel.zaguan_inmobiliarias.service.property.photo;

import com.manuel.zaguan_inmobiliarias.entity.property.photo.PropertyPhoto;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.photo.PropertyPhotoNotFoundException;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.photo.JpaPropertyPhotoRepository;
import com.manuel.zaguan_inmobiliarias.storage.PhotoStorage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertyPhotoDeleteService {

    private final JpaPropertyPhotoRepository jpaPropertyPhotoRepository;
    private final JpaPropertyRepository jpaPropertyRepository;
    private final PhotoStorage photoStorage;

    public PropertyPhotoDeleteService(JpaPropertyPhotoRepository jpaPropertyPhotoRepository,
                                      JpaPropertyRepository jpaPropertyRepository,
                                      PhotoStorage photoStorage) {
        this.jpaPropertyPhotoRepository = jpaPropertyPhotoRepository;
        this.jpaPropertyRepository = jpaPropertyRepository;
        this.photoStorage = photoStorage;
    }

    @Transactional
    public void delete(Long propertyId, Long photoId) {
        if (!jpaPropertyRepository.existsByIdAndActiveTrue(propertyId)) {
            throw new PropertyNotFoundException(propertyId);
        }

        //Por id y por property: una foto de otra propiedad no se borra desde esta URL
        PropertyPhoto photo = jpaPropertyPhotoRepository.findByIdAndPropertyId(photoId, propertyId)
                .orElseThrow(() -> new PropertyPhotoNotFoundException(photoId));

        String url = photo.getUrl();

        //Primero la fila y con flush, asi un error de base salta antes de tocar el disco
        jpaPropertyPhotoRepository.delete(photo);
        jpaPropertyPhotoRepository.flush();

        //Si el archivo no se puede borrar, la excepcion hace rollback y la fila vuelve
        photoStorage.delete(url);
    }
}
