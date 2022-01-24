package com.github.throyer.common.springboot.domain.mail.service;

import com.github.throyer.common.springboot.domain.mail.model.Email;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;

import static com.github.throyer.common.springboot.utils.Constants.MAIL.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
public class MailService {

    private final TemplateEngine engine;
    private final JavaMailSender sender;

    @Autowired
    public MailService(TemplateEngine engine, JavaMailSender sender) {
        this.engine = engine;
        this.sender = sender;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    public void send(Email email) {
        try {
            var message = sender.createMimeMessage();
            var helper = new MimeMessageHelper(message);
            helper.setTo(email.getDestination());
            helper.setSubject(email.getSubject());
            helper.setText(engine.process(email.getTemplate(), email.getContext()), CONTENT_IS_HTML);
            sender.send(message);
            LOGGER.info(EMAIL_SUCCESSFULLY_SENT_TO, email.getDestination());
        } catch (MessagingException | MailException exception) {
            LOGGER.error(ERROR_SENDING_EMAIL_MESSAGE, exception);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, ERROR_SENDING_EMAIL_MESSAGE);
        }
    }
}
