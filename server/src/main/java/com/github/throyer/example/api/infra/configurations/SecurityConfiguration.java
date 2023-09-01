package com.github.throyer.example.api.infra.configurations;

import com.github.throyer.example.api.domain.authentication.services.AuthenticationService;
import com.github.throyer.example.api.infra.environments.SwaggerProperties;
import com.github.throyer.example.api.infra.middlewares.AuthenticationMiddleware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static com.github.throyer.example.api.shared.rest.PublicRoutes.PublicRoutesManager.publicRoutes;
import static com.github.throyer.example.api.shared.rest.Responses.forbidden;
import static com.github.throyer.example.api.utils.Strings.noneOfThenNullOrEmpty;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
  private final UserDetailsService userDetailsService;
  private final AuthenticationMiddleware authenticationMiddleware;
  private final PasswordEncoder encoder;
  private final SwaggerProperties swaggerProperties;
          
  @Autowired
  public SecurityConfiguration(
    AuthenticationService authenticationService,
    AuthenticationMiddleware filter,
    PasswordEncoder encoder,
    SwaggerProperties swaggerProperties
  ) {
    this.userDetailsService = authenticationService;
    this.authenticationMiddleware = filter;
    this.encoder = encoder;
    this.swaggerProperties = swaggerProperties;
  }
  
  @Autowired
  protected void globalConfiguration(AuthenticationManagerBuilder authentication) throws Exception {
    if (noneOfThenNullOrEmpty(swaggerProperties.getPassword(), swaggerProperties.getUsername())) {
      authentication
        .inMemoryAuthentication()
        .passwordEncoder(encoder)
        .withUser(swaggerProperties.getUsername())
        .password(encoder.encode(swaggerProperties.getPassword()))
        .authorities(List.of());
    }

    authentication
      .userDetailsService(userDetailsService)
      .passwordEncoder(encoder);
  }
    
  @Bean
  public SecurityFilterChain api(HttpSecurity http) throws Exception {
    publicRoutes()
      .add(GET, "/", "/docs", "/swagger-ui/**", "/v3/api-docs/**")
      .add(POST, "/users", "/authentication/**", "/recoveries/**")
    .injectOn(http);
  
    http
    .csrf(AbstractHttpConfigurer::disable)
    .authorizeHttpRequests(request -> request.anyRequest().authenticated())
    .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
    .addFilterBefore(authenticationMiddleware, UsernamePasswordAuthenticationFilter.class)
    .exceptionHandling(handler -> handler.authenticationEntryPoint((request, response, exception) -> forbidden(response)))
    .cors(configurer -> new CorsConfiguration().applyPermitDefaultValues());
    
    return http.build();
  }
  
  @Bean
  @Order(1)
  public SecurityFilterChain swagger(HttpSecurity http) throws Exception {
    if (noneOfThenNullOrEmpty(swaggerProperties.getPassword(), swaggerProperties.getUsername())) {
      http
      .securityMatcher("/swagger-ui/**")
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
      .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
      .httpBasic(withDefaults());     
    } else {
      http
      .securityMatcher("/swagger-ui/**")
      .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
    }
    
    return http.build();
  }
}
