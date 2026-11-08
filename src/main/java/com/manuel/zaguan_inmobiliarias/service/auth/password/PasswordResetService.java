package com.manuel.zaguan_inmobiliarias.service.auth.password;

import com.manuel.zaguan_inmobiliarias.dto.request.auth.password.PasswordResetRequest;
import com.manuel.zaguan_inmobiliarias.entity.auth.PasswordResetToken;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.exception.auth.InvalidPasswordResetTokenException;
import com.manuel.zaguan_inmobiliarias.repository.auth.JpaPasswordResetTokenRepository;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

//Cambia la contraseña con el token del link del mail. No pide la actual: justamente se la olvido
@Service
@AllArgsConstructor
public class PasswordResetService {
    private final JpaPasswordResetTokenRepository jpaPasswordResetTokenRepository;
    private final JpaUserRepository jpaUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        LocalDateTime now = LocalDateTime.now();
        PasswordResetToken token = jpaPasswordResetTokenRepository
                .findByTokenHash(PasswordResetTokens.hash(request.token()))
                .filter(t -> t.getUsedAt() == null && t.getExpiresAt().isAfter(now))
                .orElseThrow(InvalidPasswordResetTokenException::new);

        User user = jpaUserRepository.findByIdAndActiveTrue(token.getUserId())
                .orElseThrow(InvalidPasswordResetTokenException::new);

        //La contraseña nunca se guarda como llega: se guarda el hash de BCrypt
        user.setPassword(passwordEncoder.encode(request.password()));
        //Este y cualquier otro que quedara pendiente
        jpaPasswordResetTokenRepository.invalidateAllForUser(user.getId(), now);
    }
}
