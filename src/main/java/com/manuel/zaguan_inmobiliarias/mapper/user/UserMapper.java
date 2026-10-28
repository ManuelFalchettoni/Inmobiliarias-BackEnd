package com.manuel.zaguan_inmobiliarias.mapper.user;

import com.manuel.zaguan_inmobiliarias.dto.request.auth.user.AuthUserRequest;
import com.manuel.zaguan_inmobiliarias.dto.request.user.UserRequest;
import com.manuel.zaguan_inmobiliarias.dto.request.user.UserUpdateRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.enums.user.UserRol;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import com.manuel.zaguan_inmobiliarias.storage.PhotoStorage;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final JpaAgencyRepository jpaAgencyRepository;
    private final PhotoStorage photoStorage;

    public UserMapper(JpaAgencyRepository jpaAgencyRepository, PhotoStorage photoStorage) {
        this.jpaAgencyRepository = jpaAgencyRepository;
        this.photoStorage = photoStorage;
    }

    public User toEntity(UserRequest userRequest){
        User user = new User();

        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        //La contraseña no se copia aca: la hashea UserCreatorService
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setRol(userRequest.getRol());
        user.setAgencyId(userRequest.getAgencyId());
        user.setCuit(userRequest.getCuit());
        user.setLicense(userRequest.getLicense());
        user.setActive(true);
        //createdAt y updatedAt los ponen @CreationTimestamp y @UpdateTimestamp

        return user;
    }

    //Registro desde auth. AuthUserRequest no trae rol ni inmobiliaria: entra como USER y sin
    //agencyId. La contraseña la hashea AuthUserRegisterService
    public User toEntity(AuthUserRequest authUserRequest){
        User user = new User();

        user.setName(authUserRequest.getName());
        user.setEmail(authUserRequest.getEmail());
        user.setPhoneNumber(authUserRequest.getPhoneNumber());
        user.setRol(UserRol.USER);
        user.setActive(true);

        return user;
    }

    //La contraseña, el rol y active no se tocan al editar: cada uno tiene su endpoint.
    //agencyId tampoco: un usuario no cambia de inmobiliaria
    public void updateEntity(UserUpdateRequest userUpdateRequest, User user){
        user.setName(userUpdateRequest.getName());
        user.setEmail(userUpdateRequest.getEmail());
        user.setPhoneNumber(userUpdateRequest.getPhoneNumber());
        user.setCuit(userUpdateRequest.getCuit());
        user.setLicense(userUpdateRequest.getLicense());
    }

    public UserResponse toResponse(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        //UserResponse.rol es String: se manda el nombre del enum, igual que JwtService
        userResponse.setRol(user.getRol() != null ? user.getRol().name() : null);
        userResponse.setAgencyId(user.getAgencyId());
        userResponse.setAgencyIconUrl(agencyIconUrlOf(user.getAgencyId()));
        userResponse.setCuit(user.getCuit());
        userResponse.setLicense(user.getLicense());
        userResponse.setActive(user.getActive());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;
    }

    //El logo es de la inmobiliaria: cada usuario muestra el de su agencyId. Se consulta solo la
    //columna icon, sin traer la Agency entera. Null si no tiene inmobiliaria o si no tiene logo
    private String agencyIconUrlOf(Long agencyId) {
        if (agencyId == null) {
            return null;
        }
        return jpaAgencyRepository.findIconById(agencyId)
                .map(photoStorage::urlOf)
                .orElse(null);
    }

}
