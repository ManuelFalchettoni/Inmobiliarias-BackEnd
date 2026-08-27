package com.manuel.zaguan_inmobiliarias.service.user;

import com.manuel.zaguan_inmobiliarias.dto.request.user.UserRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.mapper.user.UserMapper;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserCreatorService {
    public final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    public UserResponse creator(UserRequest userRequest){
        User user = jpaUserRepository.save(userMapper.toEntity(userRequest));
        return userMapper.toResponse(user);
    }

}
