package com.manuel.zaguan_inmobiliarias.service.property.owner;

import com.manuel.zaguan_inmobiliarias.entity.property.Owner.PropertyOwner;
import com.manuel.zaguan_inmobiliarias.exception.property.owner.PropertyOwnerNotFoundException;
import com.manuel.zaguan_inmobiliarias.repository.property.owner.JpaPropertyOwnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//Borrado fisico: es solo el vinculo entre una persona y una propiedad, no hay nada que
//restaurar. Si vuelve a ser dueña se carga de nuevo
@Service
@AllArgsConstructor
public class PropertyOwnerDeleterService {
    private final JpaPropertyOwnerRepository jpaPropertyOwnerRepository;

    @Transactional
    public void delete(Long id){
        PropertyOwner propertyOwner = jpaPropertyOwnerRepository.findById(id)
                .orElseThrow(() -> new PropertyOwnerNotFoundException(id));

        jpaPropertyOwnerRepository.delete(propertyOwner);
    }
}
