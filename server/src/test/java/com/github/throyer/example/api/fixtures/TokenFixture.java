package com.github.throyer.example.api.fixtures;

import com.github.throyer.example.api.shared.jwt.JWT;
import com.github.throyer.example.api.shared.jwt.JsonWebTokenImplementation;
import com.github.throyer.example.api.utils.ID;
import com.github.throyer.example.api.utils.Random;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.throyer.example.api.utils.Random.between;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Component
public class TokenFixture {
  private static final JWT jwt = new JsonWebTokenImplementation();
  
  public static String token(Long id, String roles, String secret) {
    return token(id, now().plusHours(24), secret, List.of(roles.split(",")));
  }

  public static String token(String roles, String secret) {
    return token(between(1, 9999).longValue(), now().plusHours(24), secret, List.of(roles.split(",")));
  }

  public static String token(LocalDateTime expiration, String roles, String secret) {
    return token(between(1, 9999).longValue(), expiration, secret, List.of(roles.split(",")));
  }

  public static String token(Long id, LocalDateTime expiration, String secret, List<String> roles) {
    var token = jwt.encode(ID.encode(id), roles, expiration, secret);
    return format("Bearer %s", token);
  }
}
