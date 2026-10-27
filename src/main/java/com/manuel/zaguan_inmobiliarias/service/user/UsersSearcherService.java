package com.manuel.zaguan_inmobiliarias.service.user;

import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.mapper.user.UserMapper;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UsersSearcherService {
    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;
    private final AgencyScopeService agencyScopeService;

    //agencyId lo respeta solo el admin; en null lista todos
    public Page<UserResponse> findAll(Long agencyId, Boolean active, Pageable pageable){
        Long scopedAgencyId = agencyScopeService.resolveAgencyId(agencyId);

        Page<User> users = (scopedAgencyId == null)
                ? jpaUserRepository.findAllByActive(active, pageable)
                : jpaUserRepository.findAllByAgencyIdAndActive(scopedAgencyId, active, pageable);
        return users.map(
                userMapper::toResponse
        );
    }
}
