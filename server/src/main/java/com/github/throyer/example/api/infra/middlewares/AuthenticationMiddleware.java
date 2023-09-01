package com.github.throyer.example.api.infra.middlewares;

import com.github.throyer.example.api.infra.security.RequestAuthorizer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Order(1)
@Component
@AllArgsConstructor
public class AuthenticationMiddleware extends OncePerRequestFilter {
  private final RequestAuthorizer authorizer;
  
  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filter
  ) throws ServletException, IOException {
    authorizer.tryAuthorize(request, response);
    filter.doFilter(request, response);
  }
}
