package com.manuel.zaguan_inmobiliarias.service.crm;

import com.manuel.zaguan_inmobiliarias.dto.request.email.EmailRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.email.EmailResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmAlert;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmProperty;
import com.manuel.zaguan_inmobiliarias.entity.emailtemplate.EmailTemplate;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.enums.emailtemplate.EmailTemplateKeysEnum;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmAlertRepository;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.emailtemplate.JpaEmailTemplateRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import com.manuel.zaguan_inmobiliarias.service.email.EmailSenderService;
import com.manuel.zaguan_inmobiliarias.service.email.EmailVariableResolverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//Le avisa por email al agente que tiene una alerta por vencer.
//Corre sin usuario logueado, asi que usa los repositorios y no AgencyScopeService, que lee el
//usuario del token. Sin @Transactional: no hay que tener una transaccion abierta mientras se
//espera al SMTP. Si el envio falla, notifiedAt queda en null y se reintenta en la proxima corrida
@Slf4j
@Service
@RequiredArgsConstructor
public class CrmAlertReminderService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String STATUS_SENT = "SENT";

    private final JpaCrmAlertRepository jpaCrmAlertRepository;
    private final JpaCrmPropertyRepository jpaCrmPropertyRepository;
    private final JpaPropertyRepository jpaPropertyRepository;
    private final JpaUserRepository jpaUserRepository;
    private final JpaEmailTemplateRepository jpaEmailTemplateRepository;
    private final EmailSenderService emailSenderService;
    private final EmailVariableResolverService emailVariableResolverService;

    @Value("${app.crm-alerts.reminder-hours}")
    private int reminderHours;

    //initialDelay: le da tiempo a EmailTemplateInitializer de crear la plantilla al arrancar
    @Scheduled(initialDelay = 60000, fixedDelayString = "${app.crm-alerts.check-interval-ms}")
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<CrmAlert> alerts = jpaCrmAlertRepository.findToNotify(now, now.plusHours(reminderHours));
        if (alerts.isEmpty()) {
            return;
        }

        Optional<EmailTemplate> template = jpaEmailTemplateRepository.findByKey(EmailTemplateKeysEnum.CRM_ALERT_REMINDER);
        if (template.isEmpty()) {
            log.warn("No existe la plantilla {}, no se mandan recordatorios", EmailTemplateKeysEnum.CRM_ALERT_REMINDER);
            return;
        }

        //Un error en una alerta no frena a las demas
        for (CrmAlert alert : alerts) {
            try {
                sendReminder(alert, template.get());
            } catch (RuntimeException e) {
                log.error("No se pudo procesar el recordatorio de la alerta {}", alert.getId(), e);
            }
        }
    }

    private void sendReminder(CrmAlert alert, EmailTemplate template) {
        //Un agente dado de baja no recibe mails. La alerta se reintenta sola si se la pasan a otro
        Optional<User> user = jpaUserRepository.findByIdAndActiveTrue(alert.getUserId());
        if (user.isEmpty()) {
            log.debug("La alerta {} es de un agente dado de baja", alert.getId());
            return;
        }

        Map<String, String> variables = Map.of(
                "agentName", user.get().getName(),
                "message", alert.getMessage(),
                "alertDate", alert.getAlertDate().format(DATE_FORMAT),
                "propertyAddress", findPropertyAddress(alert));

        EmailResponse response = emailSenderService.sendEmail(new EmailRequest(
                user.get().getEmail(),
                null,
                emailVariableResolverService.resolve(template.getSubject(), variables),
                emailVariableResolverService.resolveHtml(template.getBody(), variables),
                true));

        if (STATUS_SENT.equals(response.status())) {
            jpaCrmAlertRepository.markNotified(alert.getId(), LocalDateTime.now());
        }
    }

    //Ids sueltos, sin FK: si el lead o la propiedad ya no estan, el aviso sale igual
    private String findPropertyAddress(CrmAlert alert) {
        return jpaCrmPropertyRepository.findById(alert.getCrmPropertyId())
                .map(CrmProperty::getPropertyId)
                .flatMap(jpaPropertyRepository::findById)
                .map(Property::getAddress)
                .orElse("");
    }
}
