package com.github.throyer.example.api.infra.security;

import com.github.throyer.example.api.infra.environments.SecurityProperties;
import com.github.throyer.example.api.shared.jwt.JWT;
import com.github.throyer.example.api.shared.rest.PublicRoutes;
import com.github.throyer.example.api.utils.Authorization;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.github.throyer.example.api.shared.rest.PublicRoutes.PublicRoutesManager.publicRoutes;
import static com.github.throyer.example.api.shared.rest.Responses.expired;
import static java.util.Objects.isNull;

public class RequestAuthorizerImpl implements RequestAuthorizer {
  private final JWT jwt;
  private final SecurityProperties properties;
  
  public RequestAuthorizerImpl(JWT jwt, SecurityProperties properties) {
    this.jwt = jwt;
    this.properties = properties;
  }
  
  @Override
  public void tryAuthorize(HttpServletRequest request, HttpServletResponse response) {
    if (publicRoutes().anyMatch(request)) {
      return;
    }

    var token = Authorization.extract(request);

    if (isNull(token)) {
      return;
    }

    try {
      var authorized = jwt.decode(token, properties.getTokenSecret());
      SecurityContextHolder
      .getContext()
      .setAuthentication(authorized.getAuthentication());
    } catch (Exception exception) {
      expired(response);
    }
  }
}
