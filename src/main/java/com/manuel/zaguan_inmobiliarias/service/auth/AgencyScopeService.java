package com.manuel.zaguan_inmobiliarias.service.auth;

import com.manuel.zaguan_inmobiliarias.config.UserPrincipal;
import com.manuel.zaguan_inmobiliarias.enums.user.UserRol;
import com.manuel.zaguan_inmobiliarias.exception.auth.ForbiddenActionException;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.people.JpaPeopleRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.contract.JpaPropertyContractRepository;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

//La regla de que ve cada uno, en un solo lugar:
//- ADMIN: todo. En listados y busquedas elige la inmobiliaria con agencyId, o ve todas
//- AGENCY y AGENT: solo lo de su inmobiliaria. El agencyId que manden se ignora: sale de su usuario
//El usuario sale del token (lo carga JwtFilter), asi que no se puede falsear desde el request.
//SecurityConfig ya corta a los que no estan logueados o no son de estos roles
@Service
@AllArgsConstructor
public class AgencyScopeService {
    private final JpaPropertyRepository jpaPropertyRepository;
    private final JpaPeopleRepository jpaPeopleRepository;
    private final JpaUserRepository jpaUserRepository;
    private final JpaPropertyContractRepository jpaPropertyContractRepository;
    private final JpaCrmPropertyRepository jpaCrmPropertyRepository;

    //Para listados, busquedas y altas: en que inmobiliaria se busca o se carga.
    //En un listado del admin, null es "todas"
    public Long resolveAgencyId(Long requestedAgencyId) {
        if (isAdmin()) {
            return requestedAgencyId;
        }
        return currentAgencyId();
    }

    //Para traer, editar o borrar algo: si es de la inmobiliaria del usuario
    public boolean canAccess(Long agencyId) {
        if (isAdmin()) {
            return true;
        }
        return Objects.equals(currentAgencyId(), agencyId);
    }

    public boolean isAdmin() {
        return currentUser().getRol() == UserRol.ADMIN;
    }

    //Los canAccess de abajo dan false si el registro no existe, asi reemplazan a los existsById:
    //una propiedad de otra inmobiliaria responde 404, igual que una que no existe

    public boolean canAccessProperty(Long propertyId) {
        return jpaPropertyRepository.findById(propertyId)
                .map(property -> canAccess(property.getAgencyId()))
                .orElse(false);
    }

    public boolean canAccessActiveProperty(Long propertyId) {
        return jpaPropertyRepository.findByIdAndActiveTrue(propertyId)
                .map(property -> canAccess(property.getAgencyId()))
                .orElse(false);
    }

    public boolean canAccessPeople(Long peopleId) {
        return jpaPeopleRepository.findById(peopleId)
                .map(people -> canAccess(people.getAgencyId()))
                .orElse(false);
    }

    public boolean canAccessActiveUser(Long userId) {
        return jpaUserRepository.findByIdAndActiveTrue(userId)
                .map(user -> canAccess(user.getAgencyId()))
                .orElse(false);
    }

    //El contrato y el lead no tienen agencyId propio: es el de su propiedad
    public boolean canAccessContract(Long contractId) {
        return jpaPropertyContractRepository.findById(contractId)
                .map(contract -> canAccessProperty(contract.getPropertyId()))
                .orElse(false);
    }

    public boolean canAccessCrmProperty(Long crmPropertyId) {
        return jpaCrmPropertyRepository.findById(crmPropertyId)
                .map(crmProperty -> canAccessProperty(crmProperty.getPropertyId()))
                .orElse(false);
    }

    //Un AGENCY o AGENT sin inmobiliaria no deberia existir (UserRequest la pide), pero si
    //aparece no puede ver nada: con null, resolveAgencyId le mostraria todas
    private Long currentAgencyId() {
        Long agencyId = currentUser().getAgencyId();
        if (agencyId == null) {
            throw new ForbiddenActionException("User without agency");
        }
        return agencyId;
    }

    private UserPrincipal currentUser() {
        return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
