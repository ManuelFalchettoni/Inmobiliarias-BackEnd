package com.manuel.zaguan_inmobiliarias.service.auth.user;

import com.manuel.zaguan_inmobiliarias.dto.request.auth.user.AuthUserLoginRequest;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.exception.auth.InvalidCredentialsException;
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

    //Email inexistente, usuario dado de baja o contraseña incorrecta dan el mismo error
    public String login(AuthUserLoginRequest loginRequest) {
        User user = jpaUserRepository.findByEmailAndActiveTrue(loginRequest.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return jwtService.generateToken(user.getEmail(), user.getRol(), user.getId(), user.getName());
    }
}
