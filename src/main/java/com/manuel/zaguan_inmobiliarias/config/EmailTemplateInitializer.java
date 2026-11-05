package com.manuel.zaguan_inmobiliarias.config;

import com.manuel.zaguan_inmobiliarias.entity.emailtemplate.EmailTemplate;
import com.manuel.zaguan_inmobiliarias.enums.emailtemplate.EmailTemplateKeysEnum;
import com.manuel.zaguan_inmobiliarias.repository.emailtemplate.JpaEmailTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//Crea las plantillas que falten al arrancar. No va en DataSeeder: ese corre solo con la base
//vacia y se puede apagar, y estas plantillas hacen falta en todos los ambientes.
//Una que ya existe no se pisa, asi se puede editar en la base
@Component
@RequiredArgsConstructor
public class EmailTemplateInitializer implements CommandLineRunner {

    private static final String CRM_ALERT_REMINDER_SUBJECT = "Recordatorio: tenés una alerta por vencer";

    private static final String CRM_ALERT_REMINDER_BODY = """
            <p>Hola {{agentName}},</p>
            <p>Tenés una alerta que vence el <strong>{{alertDate}}</strong>:</p>
            <p>{{message}}</p>
            <p>Propiedad: {{propertyAddress}}</p>
            """;

    private final JpaEmailTemplateRepository jpaEmailTemplateRepository;

    @Override
    public void run(String... args) {
        createIfMissing(EmailTemplateKeysEnum.CRM_ALERT_REMINDER, CRM_ALERT_REMINDER_SUBJECT, CRM_ALERT_REMINDER_BODY);
    }

    private void createIfMissing(EmailTemplateKeysEnum key, String subject, String body) {
        if (jpaEmailTemplateRepository.findByKey(key).isPresent()) {
            return;
        }

        EmailTemplate template = new EmailTemplate();
        template.setKey(key);
        template.setSubject(subject);
        template.setBody(body);
        jpaEmailTemplateRepository.save(template);
    }
}
