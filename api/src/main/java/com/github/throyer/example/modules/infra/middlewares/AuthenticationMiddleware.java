package com.github.throyer.example.modules.infra.middlewares;

import static com.github.throyer.example.modules.authentication.services.RequestAuthorizer.tryAuthorizeRequest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Order(1)
@Component
public class AuthenticationMiddleware extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filter
  ) throws ServletException, IOException {
    tryAuthorizeRequest(request, response);
    filter.doFilter(request, response);
  }
}
