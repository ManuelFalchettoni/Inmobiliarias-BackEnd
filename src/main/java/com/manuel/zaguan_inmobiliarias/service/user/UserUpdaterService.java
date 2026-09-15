package com.manuel.zaguan_inmobiliarias.service.user;

import com.manuel.zaguan_inmobiliarias.dto.request.user.UserRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.exception.user.UserAlreadyExistsException;
import com.manuel.zaguan_inmobiliarias.exception.user.UserNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.user.UserMapper;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class UserUpdaterService {
    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse update(Long id, UserRequest userRequest){
        User toUpdate = jpaUserRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException(id));

        //Los controles van antes de los set: si el usuario ya tiene los datos nuevos, la consulta
        //exists haria flush y saltaria el error de la base antes que el nuestro
        if (jpaUserRepository.existsByEmailAndIdNot(userRequest.getEmail(), id)) {
            throw new UserAlreadyExistsException("Email already registered: " + userRequest.getEmail());
        }
        if (jpaUserRepository.existsByPhoneNumberAndIdNot(userRequest.getPhoneNumber(), id)) {
            throw new UserAlreadyExistsException("Phone number already registered: " + userRequest.getPhoneNumber());
        }

        toUpdate.setName(userRequest.getName());
        toUpdate.setEmail(userRequest.getEmail());
        toUpdate.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        toUpdate.setPhoneNumber(userRequest.getPhoneNumber());
        LocalDateTime now = LocalDateTime.now();
        toUpdate.setUpdatedAt(now);

        return userMapper.toResponse(jpaUserRepository.save(toUpdate));
    }
}
