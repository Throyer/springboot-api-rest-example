package com.github.throyer.example.api.infra.environments;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
  private String tokenSecret;
  private String hashidSecret;
  private Integer tokenExpirationInHours;
  private Integer refreshTokenExpirationInDays;
}
