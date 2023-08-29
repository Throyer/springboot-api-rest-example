package com.github.throyer.example.api.shared.password;

public interface PasswordEncoder {
  String encode(String password);
  Boolean matches(String raw, String encodedPassword);
}
