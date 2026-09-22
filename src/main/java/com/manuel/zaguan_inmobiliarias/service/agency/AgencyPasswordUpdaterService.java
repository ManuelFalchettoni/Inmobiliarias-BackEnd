package com.manuel.zaguan_inmobiliarias.service.agency;

import com.manuel.zaguan_inmobiliarias.dto.request.agency.AgencyPasswordRequest;
import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.exception.InvalidCurrentPasswordException;
import com.manuel.zaguan_inmobiliarias.exception.agency.AgencyNotFoundException;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//Cambiar la contraseña es lo unico que hace: el PUT de datos ya no la toca
@Service
@AllArgsConstructor
public class AgencyPasswordUpdaterService {
    private final JpaAgencyRepository jpaAgencyRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional //Necesario porque no hay un metodo save para guardar el cambio
    public void updatePassword(Long id, AgencyPasswordRequest agencyPasswordRequest){
        Agency agency = jpaAgencyRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new AgencyNotFoundException(id));

        //Se compara contra el hash guardado: la actual tiene que coincidir para poder cambiarla
        if (!passwordEncoder.matches(agencyPasswordRequest.getCurrentPassword(), agency.getPassword())) {
            throw new InvalidCurrentPasswordException();
        }

        //La contraseña nunca se guarda como llega: se guarda el hash de BCrypt
        agency.setPassword(passwordEncoder.encode(agencyPasswordRequest.getPassword()));
    }
}
