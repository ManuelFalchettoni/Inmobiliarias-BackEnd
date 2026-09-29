package com.manuel.zaguan_inmobiliarias.service.user;

import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.exception.user.UserNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.user.UserMapper;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserRestoreService {
    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse restore(Long id){
        //findById pelado: si el usuario ya estaba activo no hace nada y devuelve 200 igual
        User user = jpaUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setActive(true);

        //Flush para que updatedAt salga actualizado en la respuesta
        jpaUserRepository.saveAndFlush(user);
        return userMapper.toResponse(user);
    }
}
