package com.github.throyer.common.springboot.domain.services.email;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;

@Service
public class MailService {
    @Autowired
    private TemplateEngine engine;

    @Autowired
    private JavaMailSender sender;

    private static Logger LOGGER = LoggerFactory.getLogger(MailService.class);
    private static String ERROR_MESSAGE = "Erro ao enviar email.";
    private static final Boolean CONTENT_IS_HTML = true;

    public void send(Email email) {
        try {
            var message = sender.createMimeMessage();
            var helper = new MimeMessageHelper(message);
            helper.setTo(email.getDestination());
            helper.setSubject(email.getSubject());
            helper.setText(engine.process(email.getTemplate(), email.getContext()), CONTENT_IS_HTML);
            sender.send(message);
            LOGGER.info("Email enviado com sucesso para: {}", email.getDestination());
        } catch (MessagingException | MailException exception) {
            LOGGER.error(ERROR_MESSAGE, exception);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
        }
    }
}
