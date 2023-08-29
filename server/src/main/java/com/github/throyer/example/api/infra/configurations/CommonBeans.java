package com.github.throyer.example.api.infra.configurations;

import com.github.throyer.example.api.infra.environments.SecurityProperties;
import com.github.throyer.example.api.shared.identity.IdentityEncoder;
import com.github.throyer.example.api.shared.identity.IdentityEncoderWithHashIds;
import com.github.throyer.example.api.shared.jwt.JWT;
import com.github.throyer.example.api.shared.jwt.JsonWebTokenImplementation;
import com.github.throyer.example.api.shared.password.PasswordEncoder;
import com.github.throyer.example.api.shared.password.PasswordEncoderWithBCrypt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonBeans {
  @Bean
  IdentityEncoder id(SecurityProperties properties) {
    return new IdentityEncoderWithHashIds(properties);
  }

  @Bean
  PasswordEncoder password() {
    return new PasswordEncoderWithBCrypt();
  }
  
  @Bean
  JWT jwt() {
    return new JsonWebTokenImplementation();
  }
}
