package com.manuel.zaguan_inmobiliarias.service.agency;

import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.exception.agency.AgencyNotFoundException;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AgencyDeleterService {
    private final JpaAgencyRepository jpaAgencyRepository;

    //Baja logica, como en Property: la fila no se borra nunca. Property.idAgency es un id
    //suelto, sin FK, asi que el delete fisico dejaba propiedades apuntando a una
    //inmobiliaria que ya no existe
    @Transactional //Necesario porque no hay un metodo save para guardar el cambio
    public void delete (Long id){
        Agency agency = jpaAgencyRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new AgencyNotFoundException(id));

        agency.setActive(false);
    }

}
