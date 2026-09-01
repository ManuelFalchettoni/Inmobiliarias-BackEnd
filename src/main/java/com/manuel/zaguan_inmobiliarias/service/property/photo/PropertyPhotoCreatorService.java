package com.manuel.zaguan_inmobiliarias.service.property.photo;

import com.manuel.zaguan_inmobiliarias.dto.response.property.photo.PropertyPhotoResponse;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.entity.property.photo.PropertyPhoto;
import com.manuel.zaguan_inmobiliarias.exception.property.PropertyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.property.photo.InvalidPhotoException;
import com.manuel.zaguan_inmobiliarias.exception.property.photo.PhotoLimitExceededException;
import com.manuel.zaguan_inmobiliarias.mapper.property.photo.PropertyPhotoMapper;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.photo.JpaPropertyPhotoRepository;
import com.manuel.zaguan_inmobiliarias.storage.PhotoStorage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class PropertyPhotoCreatorService {
    private final JpaPropertyPhotoRepository jpaPropertyPhotoRepository;
    private final JpaPropertyRepository jpaPropertyRepository;
    private final PhotoStorage photoStorage;
    private final PropertyPhotoMapper propertyPhotoMapper;

    private static final int MAX_PHOTOS = 20;

    public PropertyPhotoCreatorService(JpaPropertyPhotoRepository jpaPropertyPhotoRepository, JpaPropertyRepository jpaPropertyRepository, PhotoStorage photoStorage, PropertyPhotoMapper propertyPhotoMapper) {

        this.jpaPropertyPhotoRepository = jpaPropertyPhotoRepository;
        this.jpaPropertyRepository = jpaPropertyRepository;
        this.photoStorage = photoStorage;
        this.propertyPhotoMapper = propertyPhotoMapper;
    }

    @Transactional
    public List<PropertyPhotoResponse> upload(Long propertyId, List<MultipartFile> files){
        if (files == null || files.isEmpty()){
            throw new InvalidPhotoException("No files received");
        }

        Property property = jpaPropertyRepository.findByIdAndActiveTrue(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));

        int countPhotos =jpaPropertyPhotoRepository.countByPropertyId(propertyId);
        if ((countPhotos + files.size()) > MAX_PHOTOS){
            throw new PhotoLimitExceededException(MAX_PHOTOS);
        }

        List<PropertyPhotoResponse> responses = new ArrayList<>();
        List<String> storedUrls = new ArrayList<>();

        //No se usa countPhotos: si se borro una foto del medio, el count repetiria una position ya ocupada
        int position = jpaPropertyPhotoRepository.findFirstByPropertyIdOrderByPositionDesc(propertyId)
                .map(photo -> photo.getPosition() + 1)
                .orElse(0);

        try {
            for (MultipartFile file : files ){
                String url = photoStorage.store(file);
                storedUrls.add(url);

                PropertyPhoto photo = new PropertyPhoto();
                photo.setUrl(url);
                photo.setPhotoName(file.getOriginalFilename());
                photo.setPosition(position++);
                photo.setProperty(property);

                PropertyPhoto saved = jpaPropertyPhotoRepository.save(photo);
                responses.add(propertyPhotoMapper.toResponse(saved));
            }
        } catch (RuntimeException e) {
            //De la base se encarga el rollback, pero los archivos que ya se escribieron hay que borrarlos a mano
            for (String url : storedUrls) {
                photoStorage.delete(url);
            }
            throw e;
        }

        return responses;
    }
}
