package com.manuel.zaguan_inmobiliarias.service.email;

import com.manuel.zaguan_inmobiliarias.dto.request.email.EmailRequest;
import com.manuel.zaguan_inmobiliarias.dto.response.email.EmailResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class EmailSenderService {

    private final JavaMailSender mailSender;
    private final String from;

    public EmailSenderService(JavaMailSender mailSender, @Value("${app.mail.from}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public EmailResponse sendEmail(EmailRequest request) {
        String logId = UUID.randomUUID().toString();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(request.to());
            helper.setSubject(request.subject());
            helper.setText(request.body(), request.isHtml());

            if (request.cc() != null && !request.cc().isEmpty()) {
                helper.setCc(request.cc().toArray(new String[0]));
            }

            mailSender.send(message);

            return EmailResponse.success(logId, request);

        } catch (MessagingException | MailException e) {
            log.error("No se pudo enviar el email {}", logId, e);
            return EmailResponse.failure(logId, request, e.getMessage());
        }
    }
}