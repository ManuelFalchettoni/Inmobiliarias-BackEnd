package com.manuel.zaguan_inmobiliarias.service.user;

import com.manuel.zaguan_inmobiliarias.dto.request.user.UserPasswordRequest;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.exception.InvalidCurrentPasswordException;
import com.manuel.zaguan_inmobiliarias.exception.user.UserNotFoundException;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//Cambiar la contraseña es lo unico que hace: el PUT de datos ya no la toca
@Service
@AllArgsConstructor
public class UserPasswordUpdaterService {
    private final JpaUserRepository jpaUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional //Necesario porque no hay un metodo save para guardar el cambio
    public void updatePassword(Long id, UserPasswordRequest userPasswordRequest){
        User user = jpaUserRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        //Se compara contra el hash guardado: la actual tiene que coincidir para poder cambiarla
        if (!passwordEncoder.matches(userPasswordRequest.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCurrentPasswordException();
        }

        //La contraseña nunca se guarda como llega: se guarda el hash de BCrypt
        user.setPassword(passwordEncoder.encode(userPasswordRequest.getPassword()));
    }
}
