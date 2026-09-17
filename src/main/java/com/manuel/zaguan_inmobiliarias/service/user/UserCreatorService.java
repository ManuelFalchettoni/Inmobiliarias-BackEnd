package com.manuel.zaguan_inmobiliarias.service.user;

import com.manuel.zaguan_inmobiliarias.dto.request.user.UserRequest;
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
public class UserCreatorService {
    public final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    //Los exists y el save en la misma transaccion. Igual queda la red del handler de
    //DataIntegrityViolationException, para dos altas simultaneas con el mismo dato
    @Transactional
    public UserResponse creator(UserRequest userRequest){
        if (jpaUserRepository.existsByEmail(userRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered: " + userRequest.getEmail());
        }
        if (jpaUserRepository.existsByPhoneNumber(userRequest.getPhoneNumber())) {
            throw new UserAlreadyExistsException("Phone number already registered: " + userRequest.getPhoneNumber());
        }

        User user = userMapper.toEntity(userRequest);
        //La contraseña nunca se guarda como llega: se guarda el hash de BCrypt
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        return userMapper.toResponse(jpaUserRepository.save(user));
    }

}
