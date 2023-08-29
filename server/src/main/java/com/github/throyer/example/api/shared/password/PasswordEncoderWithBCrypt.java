package com.github.throyer.example.api.shared.password;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.github.throyer.example.api.infra.constants.SecurityConstants.HASH_LENGTH;

public class PasswordEncoderWithBCrypt implements PasswordEncoder {
  private final BCryptPasswordEncoder encoder;

  public PasswordEncoderWithBCrypt() {
    this.encoder = new BCryptPasswordEncoder(HASH_LENGTH);
  }

  @Override
  public String encode(String password) {
    return encoder.encode(password);
  }

  @Override
  public Boolean matches(String raw, String encodedPassword) {
    return encoder.matches(raw, encodedPassword);
  }
}
