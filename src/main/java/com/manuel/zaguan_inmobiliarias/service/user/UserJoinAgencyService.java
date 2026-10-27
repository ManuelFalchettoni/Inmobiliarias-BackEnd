package com.manuel.zaguan_inmobiliarias.service.user;

import com.manuel.zaguan_inmobiliarias.dto.request.user.UserJoinAgencyRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.enums.user.UserRol;
import com.manuel.zaguan_inmobiliarias.exception.agency.AgencyNotFoundException;
import com.manuel.zaguan_inmobiliarias.exception.agency.UserAlreadyHasAgencyException;
import com.manuel.zaguan_inmobiliarias.exception.auth.ForbiddenActionException;
import com.manuel.zaguan_inmobiliarias.exception.user.UserNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.user.UserMapper;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//El que se registra entra como USER y sin inmobiliaria (no ve nada). La inmobiliaria lo suma
//con su email y le da el rol: asi nadie se mete solo en una inmobiliaria ajena.
//JwtFilter relee el usuario en cada request, asi que el rol nuevo vale sin volver a loguearse
@Service
@AllArgsConstructor
public class UserJoinAgencyService {
    private final JpaUserRepository jpaUserRepository;
    private final JpaAgencyRepository jpaAgencyRepository;
    private final UserMapper userMapper;
    private final AgencyScopeService agencyScopeService;

    @Transactional
    public UserResponse join(UserJoinAgencyRequest request){
        //ADMIN no: ese lo crea solo un admin desde UserCreatorService
        if (request.getRol() != UserRol.AGENT && request.getRol() != UserRol.AGENCY) {
            throw new ForbiddenActionException("Only AGENT or AGENCY can be assigned");
        }

        Long agencyId = agencyScopeService.resolveAgencyId(request.getAgencyId());
        //Solo pasa con el admin, que es el unico que elige la inmobiliaria
        if (agencyId == null) {
            throw new AgencyNotFoundException("agencyId is required");
        }
        if (!jpaAgencyRepository.existsByIdAndActiveTrue(agencyId)) {
            throw new AgencyNotFoundException(agencyId);
        }

        User user = jpaUserRepository.findByEmailAndActiveTrue(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        //Uno que ya es de una inmobiliaria no se mueve desde aca, ni a la misma ni a otra
        if (user.getAgencyId() != null) {
            throw new UserAlreadyHasAgencyException("User already belongs to an agency: " + request.getEmail());
        }

        user.setAgencyId(agencyId);
        user.setRol(request.getRol());

        return userMapper.toResponse(jpaUserRepository.save(user));
    }
}
