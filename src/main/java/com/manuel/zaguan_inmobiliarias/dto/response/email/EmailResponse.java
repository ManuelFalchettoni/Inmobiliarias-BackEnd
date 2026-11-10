package com.manuel.zaguan_inmobiliarias.dto.response.email;

import com.manuel.zaguan_inmobiliarias.dto.request.email.EmailRequest;

import java.time.LocalDateTime;

public record EmailResponse (
        String id,
        String to,
        String subject,
        String status,
        String errorMessage,
        LocalDateTime sentAt
) {
    public static EmailResponse success(String id, EmailRequest req) {
        return new EmailResponse(id, req.to(), req.subject(), "SENT", null, LocalDateTime.now());
    }

    public static EmailResponse failure(String id, EmailRequest req, String error) {
        return new EmailResponse(id, req.to(), req.subject(), "FAILED", error, LocalDateTime.now());
    }
}
