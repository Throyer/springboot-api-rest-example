package com.github.throyer.example.api.infra.configurations;

import com.github.throyer.example.api.infra.environments.SecurityProperties;
import com.github.throyer.example.api.infra.security.RequestAuthorizer;
import com.github.throyer.example.api.infra.security.RequestAuthorizerImpl;
import com.github.throyer.example.api.shared.identity.IdentityEncoder;
import com.github.throyer.example.api.shared.identity.IdentityEncoderWithHashIds;
import com.github.throyer.example.api.shared.jwt.JWT;
import com.github.throyer.example.api.shared.jwt.JsonWebTokenImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.github.throyer.example.api.infra.constants.SecurityConstants.HASH_LENGTH;

@Configuration
public class CommonBeans {
  @Bean
  IdentityEncoder id(SecurityProperties properties) {
    return new IdentityEncoderWithHashIds(properties);
  }

  @Bean
  PasswordEncoder password() {
    return new BCryptPasswordEncoder(HASH_LENGTH);
  }
  
  @Bean
  JWT jwt() {
    return new JsonWebTokenImplementation();
  }
  
  @Bean
  RequestAuthorizer requestAuthorizer(JWT jwt, SecurityProperties properties) {
    return new RequestAuthorizerImpl(jwt, properties);
  }
}
