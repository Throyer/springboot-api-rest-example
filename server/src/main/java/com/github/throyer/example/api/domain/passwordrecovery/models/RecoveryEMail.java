package com.github.throyer.example.api.domain.passwordrecovery.models;

import com.github.throyer.example.api.shared.mail.models.Email;
import org.thymeleaf.TemplateEngine;

public class RecoveryEMail implements Email {
  private final String destination;
  private final String subject;
  private final String username;
  private final String code;
    
  public RecoveryEMail(
    String destination,
    String subject,
    String username,
    String code
  ) {
    this.destination = destination;
    this.subject = subject;
    this.username = username;
    this.code = code;
  }

  @Override
  public String getDestination() {
    return this.destination;
  }

  @Override
  public String getSubject() {
    return this.subject;
  }

  @Override
  public String render(TemplateEngine engine) {
    return null;
  }
}
