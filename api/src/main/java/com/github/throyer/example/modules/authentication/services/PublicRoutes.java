package com.github.throyer.example.modules.authentication.services;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PublicRoutes {
  private final Map<HttpMethod, String[]> routes = new HashMap<>();
  private final List<AntPathRequestMatcher> matchers = new ArrayList<>();

  public static PublicRoutes create() {
    return new PublicRoutes();
  }

    public PublicRoutes add(HttpMethod method, String... routes) {
      this.routes.put(method, routes);
      asList(routes)
        .forEach(route -> matchers
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
      routes.forEach((method, routes) -> {
        try {
          http
            .antMatcher("/**")
              .authorizeRequests()
                .antMatchers(method, routes)
            .permitAll();
        } catch (Exception exception) {
          log.error("error on set public routes", exception);
        }
      });
    }
}