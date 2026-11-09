package com.manuel.zaguan_inmobiliarias.repository.emailtemplate;

import com.manuel.zaguan_inmobiliarias.entity.emailtemplate.EmailTemplate;
import com.manuel.zaguan_inmobiliarias.enums.emailtemplate.EmailTemplateKeysEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaEmailTemplateRepository extends JpaRepository<EmailTemplate, String> {
    Optional<EmailTemplate> findByKey(EmailTemplateKeysEnum key);
}
