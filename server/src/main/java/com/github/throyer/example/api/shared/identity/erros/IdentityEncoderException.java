package com.github.throyer.example.api.shared.identity.erros;

public class IdentityEncoderException extends RuntimeException {  
  public IdentityEncoderException(String message) {
    super(message);
  }

  public IdentityEncoderException(String message, Throwable cause) {
    super(message, cause);
  }

  public IdentityEncoderException(Throwable cause) {
    super(cause);
  }

  public IdentityEncoderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
