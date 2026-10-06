package com.manuel.zaguan_inmobiliarias.service.auth.user;

import com.manuel.zaguan_inmobiliarias.dto.request.auth.user.AuthUserRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.exception.user.UserAlreadyExistsException;
import com.manuel.zaguan_inmobiliarias.mapper.user.UserMapper;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthUserRegisterService {
    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    //Los mismos controles que UserCreatorService, con la misma excepcion: asi el 409 sale
    //con el mismo formato y dice cual es el campo repetido
    @Transactional
    public UserResponse userRegister (AuthUserRequest userRequest){
        if (jpaUserRepository.existsByEmail(userRequest.getEmail())){
            throw new UserAlreadyExistsException("Email already registered: " + userRequest.getEmail());
        }

        if (jpaUserRepository.existsByPhoneNumber(userRequest.getPhoneNumber())) {
            throw new UserAlreadyExistsException("Phone number already registered: " + userRequest.getPhoneNumber());
        }

        User user = userMapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        return userMapper.toResponse(jpaUserRepository.save(user));
    }
}
