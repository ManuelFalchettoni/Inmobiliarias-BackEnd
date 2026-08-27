package com.manuel.zaguan_inmobiliarias.service.user;

import com.manuel.zaguan_inmobiliarias.dto.request.user.UserRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.exception.user.UserNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.user.UserMapper;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class UserUpdaterService {
    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse update(Long id, UserRequest userRequest){
        User toUpdate = jpaUserRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException(id));
        toUpdate.setName(userRequest.getName());
        toUpdate.setEmail(userRequest.getEmail());
        toUpdate.setPassword(userRequest.getPassword());
        toUpdate.setPhoneNumber(userRequest.getPhoneNumber());
        LocalDateTime now = LocalDateTime.now();
        toUpdate.setUpdatedAt(now);

        return userMapper.toResponse(jpaUserRepository.save(toUpdate));
    }
}
