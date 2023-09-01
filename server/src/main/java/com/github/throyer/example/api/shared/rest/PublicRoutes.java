package com.github.throyer.example.api.shared.rest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@Slf4j
public class PublicRoutes {
  public static class PublicRoutesManager {
    private static final PublicRoutes routes = new PublicRoutes();
    private PublicRoutesManager() { }
    
    public static PublicRoutes publicRoutes() {
      return PublicRoutesManager.routes;
    }
  }
  
  private final Map<HttpMethod, String[]> routes = new HashMap<>();
  private final List<AntPathRequestMatcher> matchers = new ArrayList<>();
    
  public PublicRoutes add(HttpMethod method, String... routes) {
    this.routes.put(method, routes);

    asList(routes)
    .forEach(route -> this.matchers
    .add(new AntPathRequestMatcher(route, method.name())));

    return this;
  }

  public boolean anyMatch(HttpServletRequest request) {
    try {
      return this.matchers.stream().anyMatch(requestMatcher -> requestMatcher.matches(request));
    } catch (Exception exception) {
      log.error("error on route matching", exception);
      return false;
    }
  }

  public void injectOn(HttpSecurity http) {
    this.routes.forEach((method, routes) -> {
      try {
        http
        .securityMatcher("/**")
        .authorizeHttpRequests(authorization -> authorization
          .requestMatchers(method, routes)
            .permitAll());
      } catch (Exception exception) {
        log.error("error on set public routes", exception);
      }
    });
  }
}
