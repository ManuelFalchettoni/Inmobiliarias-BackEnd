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
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserFinderService {
    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    //Un usuario dado de baja responde 404. Para encontrarlo hay que listar con active=false,
    //igual que en Property
    public UserResponse findById(Long id){
         User user = jpaUserRepository.findByIdAndActiveTrue(id)
                .orElseThrow(()-> new UserNotFoundException(id));
         return userMapper.toResponse(user);
    }
}
