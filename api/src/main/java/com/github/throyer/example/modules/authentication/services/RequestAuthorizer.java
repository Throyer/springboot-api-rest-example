package com.github.throyer.example.modules.authentication.services;

import static com.github.throyer.example.modules.infra.environments.SecurityEnvironments.JWT;
import static com.github.throyer.example.modules.infra.environments.SecurityEnvironments.TOKEN_SECRET;
import static com.github.throyer.example.modules.infra.http.Responses.expired;
import static com.github.throyer.example.modules.infra.http.context.HttpContext.publicRoutes;
import static java.util.Objects.isNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;

import com.github.throyer.example.modules.authentication.utils.Authorization;


public class RequestAuthorizer {
  public static void tryAuthorizeRequest(
    HttpServletRequest request,
    HttpServletResponse response
  ) {
    if (publicRoutes().anyMatch(request)) {
      return;
    }

    var token = Authorization.extract(request);

    if (isNull(token)) {
      return;
    }

    try {
      var authorized = JWT.decode(token, TOKEN_SECRET);
      SecurityContextHolder
        .getContext()
          .setAuthentication(authorized.getAuthentication());
    } catch (Exception exception) {
      expired(response);
    }
  }
}
