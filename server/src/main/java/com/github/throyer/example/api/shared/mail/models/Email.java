package com.github.throyer.example.api.shared.mail.models;

import org.thymeleaf.TemplateEngine;

public interface Email {
  String getDestination();
  String getSubject();
  String render(TemplateEngine engine);
}
