package com.github.throyer.example.api.infra.constants;

public class MessageConstants {
  public class Users {
    private Users() { }

    public static final String EMAIL_UNAVAILABLE_MESSAGE = "users.email-unavailable.message";
  }

  public class Authentication {
    private Authentication() { }

    public static final String INVALID_USERNAME_OR_PASSWORD_MESSAGE = "authentication.invalid-username-or-password.message";
    public static final String RECOVERY_CODE_EXPIRED_OR_INVALID_MESSAGE = "authentication.code-invalid-or-expired.message";
    public static final String EMAIL_WAS_NOT_CONFIRMED_MESSAGE = "authentication.email-was-not-confirmed.message";
  }

  public class Recovery {
    private Recovery() { }

    public static final String EMAIL_SUBJECT_MESSAGE = "recovery.email-subject-message";
    public static final String INVALID_OR_EXPIRED_CODE_MESSAGE = "recovery.code-invalid-or-expired.message";
  }
}
