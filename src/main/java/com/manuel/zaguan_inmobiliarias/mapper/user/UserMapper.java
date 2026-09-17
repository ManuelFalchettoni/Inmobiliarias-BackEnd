package com.manuel.zaguan_inmobiliarias.mapper.user;

import com.manuel.zaguan_inmobiliarias.dto.request.user.UserRequest;
import com.manuel.zaguan_inmobiliarias.dto.request.user.UserUpdateRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.user.UserResponse;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(UserRequest userRequest){
        User user = new User();

        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        //La contraseña no se copia aca: la hashea UserCreatorService
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setRol(userRequest.getRol());
        user.setActive(true);
        //createdAt y updatedAt los ponen @CreationTimestamp y @UpdateTimestamp

        return user;
    }

    //La contraseña, el rol y active no se tocan al editar: cada uno tiene su endpoint
    public void updateEntity(UserUpdateRequest userUpdateRequest, User user){
        user.setName(userUpdateRequest.getName());
        user.setEmail(userUpdateRequest.getEmail());
        user.setPhoneNumber(userUpdateRequest.getPhoneNumber());
    }

    public UserResponse toResponse(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setRol(user.getRol());
        userResponse.setActive(user.isActive());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;
    }

}
