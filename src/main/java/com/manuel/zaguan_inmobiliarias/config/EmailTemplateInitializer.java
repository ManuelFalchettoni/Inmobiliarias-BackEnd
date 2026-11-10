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

    private static final String PASSWORD_RESET_SUBJECT = "Restablecé tu contraseña";

    //El link pasa por resolveHtml: el token es Base64 URL-safe, el escape no le cambia nada
    private static final String PASSWORD_RESET_BODY = """
            <p>Hola {{userName}},</p>
            <p>Recibimos un pedido para restablecer la contraseña de tu cuenta.</p>
            <p><a href="{{resetLink}}">Elegí una contraseña nueva</a></p>
            <p>El link vence en {{expirationMinutes}} minutos y sirve una sola vez.</p>
            <p>Si no lo pediste vos, ignorá este mail: tu contraseña no cambia.</p>
            """;

    private final JpaEmailTemplateRepository jpaEmailTemplateRepository;

    @Override
    public void run(String... args) {
        createIfMissing(EmailTemplateKeysEnum.CRM_ALERT_REMINDER, CRM_ALERT_REMINDER_SUBJECT, CRM_ALERT_REMINDER_BODY);
        createIfMissing(EmailTemplateKeysEnum.PASSWORD_RESET, PASSWORD_RESET_SUBJECT, PASSWORD_RESET_BODY);
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
