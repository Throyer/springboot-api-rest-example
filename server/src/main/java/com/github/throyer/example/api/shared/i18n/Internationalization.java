package com.github.throyer.example.api.shared.i18n;

public interface Internationalization {
  String message(String key);
  String message(String key, Object... args);
  
  String CREATE_SESSION_USERNAME_OR_PASSWORD_MESSAGE = "create.session.error.username-or-password.message";
  String CREATE_REFRESH_CODE_INVALID_OR_EXPIRED_MESSAGE = "refresh.session.error.code-invalid-or-expired.message";
  String CREATE_OR_REFRESH_EMAIL_WAS_NOT_CONFIRMED = "create-or-refresh.session.error.email-was-not-confirmed.message";
  
  String CREATE_USER_EMAIL_UNAVAILABLE_MESSAGE = "create.user.error.email-unavailable.message";
  String CREATE_USER_FAIL_ON_QUERY_USER_ROLE = "create.user.error.role-user-not-found.message";
}
