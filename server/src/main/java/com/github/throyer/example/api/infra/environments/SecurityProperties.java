package com.github.throyer.example.api.infra.environments;

import io.jsonwebtoken.security.WeakKeyException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.nio.charset.StandardCharsets.UTF_8;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
  private String tokenSecret;
  private String hashidSecret;
  private Integer tokenExpirationInHours;
  private Integer refreshTokenExpirationInDays;

//  public void setTokenSecret(String tokenSecret) {
//    hmacShaKeyFor(tokenSecret.getBytes(UTF_8));
//    this.tokenSecret = tokenSecret;  
//  }
}
