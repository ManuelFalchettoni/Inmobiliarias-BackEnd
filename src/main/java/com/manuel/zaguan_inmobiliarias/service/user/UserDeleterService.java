package com.manuel.zaguan_inmobiliarias.service.user;

import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.exception.auth.ForbiddenActionException;
import com.manuel.zaguan_inmobiliarias.exception.user.UserNotFoundException;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserDeleterService {
    private final JpaUserRepository jpaUserRepository;
    private final AgencyScopeService agencyScopeService;

    //Baja logica, como en Property: la fila no se borra, queda con active en false.
    //El campo active ya existia en la entidad pero no lo usaba nadie
    @Transactional //Necesario porque no hay un metodo save para guardar el cambio
    public void deleter(Long id){
        //Nadie se puede dar de baja a si mismo: JwtFilter le corta la sesion en el acto, y si era el
        //unico AGENCY (o ADMIN) no queda quien administre la inmobiliaria ni quien lo restaure
        if (id.equals(agencyScopeService.currentUserId())) {
            throw new ForbiddenActionException("You cannot deactivate your own user");
        }

        User user = jpaUserRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        //Uno de otra inmobiliaria responde 404, igual que uno que no existe
        if (!agencyScopeService.canAccess(user.getAgencyId())) {
            throw new UserNotFoundException(id);
        }

        user.setActive(false);
    }

}
