package com.manuel.zaguan_inmobiliarias.service.auth.user;

import com.manuel.zaguan_inmobiliarias.dto.request.auth.user.AuthUserLoginRequest;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.exception.InvalidCurrentPasswordException;
import com.manuel.zaguan_inmobiliarias.exception.user.UserNotFoundException;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import com.manuel.zaguan_inmobiliarias.service.jwt.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthUserLoginService {
    private final JpaUserRepository jpaUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String login(AuthUserLoginRequest loginRequest) {
        User user = jpaUserRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with email " + loginRequest.getEmail() + " not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            System.out.println("Wrong password");
            throw new InvalidCurrentPasswordException();
        }

        return jwtService.generateToken(user.getEmail(), user.getRol(), user.getId(), user.getName());
    }
}

