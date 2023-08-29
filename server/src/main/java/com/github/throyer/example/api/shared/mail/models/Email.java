package com.github.throyer.example.api.shared.mail.models;

public interface Email {
  String getDestination();
  String getSubject();
  String getTemplate();
  String render();
}
