package com.manuel.zaguan_inmobiliarias.service.property;

import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import org.springframework.stereotype.Service;

@Service
public class PropertyDeleteService {
    private final JpaPropertyRepository jpaPropertyRepository;

    public PropertyDeleteService (JpaPropertyRepository jpaPropertyRepository){
        this.jpaPropertyRepository = jpaPropertyRepository;
    }

    public void delete(Long id){
        jpaPropertyRepository.deleteById(id);
    }
}
