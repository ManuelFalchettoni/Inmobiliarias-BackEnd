package com.manuel.zaguan_inmobiliarias.service.user;

import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.exception.user.UserNotFoundException;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserDeleterService {
    private final JpaUserRepository jpaUserRepository;

    //Baja logica, como en Property: la fila no se borra, queda con active en false.
    //El campo active ya existia en la entidad pero no lo usaba nadie
    @Transactional //Necesario porque no hay un metodo save para guardar el cambio
    public void deleter(Long id){
        User user = jpaUserRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setActive(false);
    }

}
