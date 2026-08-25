package com.manuel.zaguan_inmobiliarias.service.user;

import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.mapper.user.UserMapper;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsersSearcherService {
    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    public Page<UserResponse> findAll(Pageable pageable){
        Page<User> users = jpaUserRepository.findAll(pageable);
        return users.map(
                userMapper::toResponse
        );
    }
}
