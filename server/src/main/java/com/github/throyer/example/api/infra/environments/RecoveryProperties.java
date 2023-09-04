package com.github.throyer.example.api.infra.environments;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "recovery")
public class RecoveryProperties {
  private Integer minutesToExpire;
}
