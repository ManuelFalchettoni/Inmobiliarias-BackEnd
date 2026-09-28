package com.manuel.zaguan_inmobiliarias.service.user;

import com.manuel.zaguan_inmobiliarias.dto.request.user.UserUpdateRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.exception.user.UserAlreadyExistsException;
import com.manuel.zaguan_inmobiliarias.exception.user.UserNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.user.UserMapper;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class UserUpdaterService {
    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest userUpdateRequest){
        User toUpdate = jpaUserRepository.findByIdAndActiveTrue(id)
                .orElseThrow(()-> new UserNotFoundException(id));

        //Los controles van antes de los set: si el usuario ya tiene los datos nuevos, la consulta
        //exists haria flush y saltaria el error de la base antes que el nuestro
        if (jpaUserRepository.existsByEmailAndIdNot(userUpdateRequest.getEmail(), id)) {
            throw new UserAlreadyExistsException("Email already registered: " + userUpdateRequest.getEmail());
        }
        if (jpaUserRepository.existsByPhoneNumberAndIdNot(userUpdateRequest.getPhoneNumber(), id)) {
            throw new UserAlreadyExistsException("Phone number already registered: " + userUpdateRequest.getPhoneNumber());
        }

        userMapper.updateEntity(userUpdateRequest, toUpdate);

        //updatedAt lo pone @UpdateTimestamp al flushear: sin el flush la respuesta saldria con la fecha vieja
        return userMapper.toResponse(jpaUserRepository.saveAndFlush(toUpdate));
    }
}
