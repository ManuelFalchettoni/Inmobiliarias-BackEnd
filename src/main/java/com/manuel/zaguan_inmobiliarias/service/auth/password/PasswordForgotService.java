package com.manuel.zaguan_inmobiliarias.service.auth.password;

import com.manuel.zaguan_inmobiliarias.dto.request.email.EmailRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.email.EmailResponse;
import com.manuel.zaguan_inmobiliarias.entity.auth.PasswordResetToken;
import com.manuel.zaguan_inmobiliarias.entity.emailtemplate.EmailTemplate;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.enums.emailtemplate.EmailTemplateKeysEnum;
import com.manuel.zaguan_inmobiliarias.repository.auth.JpaPasswordResetTokenRepository;
import com.manuel.zaguan_inmobiliarias.repository.emailtemplate.JpaEmailTemplateRepository;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import com.manuel.zaguan_inmobiliarias.service.email.EmailSenderService;
import com.manuel.zaguan_inmobiliarias.service.email.EmailVariableResolverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

//"Olvide mi contraseña": le manda al usuario un mail con un link para elegir una nueva.
//No avisa si el email existe o no: el controller responde lo mismo en todos los casos, para que
//no se pueda averiguar que emails estan registrados
@Slf4j
@Service
public class PasswordForgotService {

    //Pedidos seguidos dentro de este lapso no mandan otro mail
    private static final int RESEND_COOLDOWN_SECONDS = 60;
    private static final String STATUS_SENT = "SENT";

    private final JpaUserRepository jpaUserRepository;
    private final JpaPasswordResetTokenRepository jpaPasswordResetTokenRepository;
    private final JpaEmailTemplateRepository jpaEmailTemplateRepository;
    private final EmailSenderService emailSenderService;
    private final EmailVariableResolverService emailVariableResolverService;
    private final String frontendUrl;
    private final int expirationMinutes;

    public PasswordForgotService(JpaUserRepository jpaUserRepository,
                                 JpaPasswordResetTokenRepository jpaPasswordResetTokenRepository,
                                 JpaEmailTemplateRepository jpaEmailTemplateRepository,
                                 EmailSenderService emailSenderService,
                                 EmailVariableResolverService emailVariableResolverService,
                                 @Value("${app.frontend-url}") String frontendUrl,
                                 @Value("${app.password-reset.expiration-minutes}") int expirationMinutes) {
        this.jpaUserRepository = jpaUserRepository;
        this.jpaPasswordResetTokenRepository = jpaPasswordResetTokenRepository;
        this.jpaEmailTemplateRepository = jpaEmailTemplateRepository;
        this.emailSenderService = emailSenderService;
        this.emailVariableResolverService = emailVariableResolverService;
        this.frontendUrl = frontendUrl.replaceAll("/+$", "");
        this.expirationMinutes = expirationMinutes;
    }

    @Transactional
    public void requestReset(String email) {
        Optional<User> user = jpaUserRepository.findByEmailAndActiveTrue(email.trim().toLowerCase(Locale.ROOT));
        if (user.isEmpty()) {
            log.debug("Pedido de recuperacion para un email sin usuario activo");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        Long userId = user.get().getId();
        if (jpaPasswordResetTokenRepository.existsByUserIdAndUsedAtIsNullAndCreatedAtAfter(
                userId, now.minusSeconds(RESEND_COOLDOWN_SECONDS))) {
            log.debug("El usuario {} ya pidio un link hace menos de {} s", userId, RESEND_COOLDOWN_SECONDS);
            return;
        }

        Optional<EmailTemplate> template = jpaEmailTemplateRepository.findByKey(EmailTemplateKeysEnum.PASSWORD_RESET);
        if (template.isEmpty()) {
            log.warn("No existe la plantilla {}, no se manda el mail de recuperacion", EmailTemplateKeysEnum.PASSWORD_RESET);
            return;
        }

        //Solo vale el ultimo link que se mando
        jpaPasswordResetTokenRepository.invalidateAllForUser(userId, now);

        String token = PasswordResetTokens.generate();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUserId(userId);
        resetToken.setTokenHash(PasswordResetTokens.hash(token));
        resetToken.setExpiresAt(now.plusMinutes(expirationMinutes));
        jpaPasswordResetTokenRepository.save(resetToken);

        Map<String, String> variables = Map.of(
                "userName", user.get().getName(),
                "resetLink", buildLink(token),
                "expirationMinutes", String.valueOf(expirationMinutes));

        EmailResponse response = emailSenderService.sendEmail(new EmailRequest(
                user.get().getEmail(),
                null,
                emailVariableResolverService.resolve(template.get().getSubject(), variables),
                emailVariableResolverService.resolveHtml(template.get().getBody(), variables),
                true));

        //Si el mail no salio, el link no le llega a nadie: se borra para que pueda pedir otro ya
        if (!STATUS_SENT.equals(response.status())) {
            jpaPasswordResetTokenRepository.delete(resetToken);
        }
    }

    private String buildLink(String token) {
        return UriComponentsBuilder.fromUriString(frontendUrl)
                .path("/restablecer-contrasena")
                .queryParam("token", token)
                .toUriString();
    }
}
