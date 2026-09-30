package com.manuel.zaguan_inmobiliarias.service.auth.user;

import com.manuel.zaguan_inmobiliarias.dto.request.auth.user.AuthUserRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.exception.auth.DuplicateResourceException;
import com.manuel.zaguan_inmobiliarias.mapper.user.UserMapper;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthUserRegisterService {
    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

public UserResponse userRegister (AuthUserRequest userRequest){
    if (jpaUserRepository.existsByEmail(userRequest.getEmail())){
        throw new DuplicateResourceException("User with email: " + userRequest.getEmail() + " already exists.");
    }

    User user = userMapper.toEntity(userRequest);
    user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

    return userMapper.toResponse(jpaUserRepository.save(user));
}
}
