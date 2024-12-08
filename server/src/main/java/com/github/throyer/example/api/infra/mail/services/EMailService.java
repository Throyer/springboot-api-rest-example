package com.github.throyer.example.api.infra.mail.services;

import static com.github.throyer.example.api.infra.constants.MailConstants.CONTENT_IS_HTML;
import static com.github.throyer.example.api.infra.constants.MailConstants.EMAIL_SUCCESSFULLY_SENT;
import static com.github.throyer.example.api.infra.constants.MailConstants.ERROR_SENDING_EMAIL_MESSAGE;
import static com.github.throyer.example.api.infra.constants.MailConstants.ERROR_SMTP_AUTH;
import static com.github.throyer.example.api.shared.rest.Responses.internalServerError;

import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import com.github.throyer.example.api.shared.mail.models.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class EMailService {
  private final TemplateEngine engine;
  private final JavaMailSender sender;

  public void send(Email email) {
    try {
      var message = createMessage(email);
      sender.send(message);
      log.info(EMAIL_SUCCESSFULLY_SENT);
      
    } catch (MailAuthenticationException exception) {
      
      log.error(ERROR_SMTP_AUTH);
      throw internalServerError(ERROR_SMTP_AUTH);
      
    } catch (MessagingException | MailException | IllegalArgumentException exception) {
      
      log.error(ERROR_SENDING_EMAIL_MESSAGE);
      throw internalServerError(ERROR_SENDING_EMAIL_MESSAGE);
    }
  }

  private MimeMessage createMessage(Email email) throws MessagingException {
    var message = sender.createMimeMessage();
    var helper = new MimeMessageHelper(message);

    var html = email.render(engine);

    helper.setTo(email.getDestination());
    helper.setSubject(email.getSubject());
    helper.setText(html, CONTENT_IS_HTML);

    return message;
  }
}
