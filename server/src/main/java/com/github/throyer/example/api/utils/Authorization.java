package com.github.throyer.example.api.utils;

import static com.github.throyer.example.api.infra.constants.SecurityConstants.ACCEPTABLE_TOKEN_TYPE;
import static com.github.throyer.example.api.infra.constants.SecurityConstants.AUTHORIZATION_HEADER;
import static com.github.throyer.example.api.infra.constants.SecurityConstants.BEARER_WORD_LENGTH;
import static com.github.throyer.example.api.infra.constants.SecurityConstants.SECURITY_TYPE;
import static java.util.Objects.isNull;

import jakarta.servlet.http.HttpServletRequest;

public class Authorization {
  private Authorization() { }

  public static String extract(HttpServletRequest request) {
    var token = request.getHeader(AUTHORIZATION_HEADER);

    if (tokenIsNull(token)) {
      return null;
    }

    if (token.substring(BEARER_WORD_LENGTH).equals(SECURITY_TYPE)) {
      return null;
    }

    return token.substring(BEARER_WORD_LENGTH);
  }

  public static boolean tokenIsNull(String token) {
    return isNull(token) || !token.startsWith(ACCEPTABLE_TOKEN_TYPE);
  }
}
