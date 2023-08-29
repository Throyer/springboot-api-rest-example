package com.github.throyer.example.api.infra.constants;

public class SecurityConstants {
  public static final Integer HASH_LENGTH = 10;

  public static final String SECURITY_TYPE = "Bearer";
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String ACCEPTABLE_TOKEN_TYPE = SECURITY_TYPE + " ";
  public static final String CAN_T_WRITE_RESPONSE_ERROR = "can't write response error.";
  public static final Integer BEARER_WORD_LENGTH = SECURITY_TYPE.length();
  
  public static class Roles {
    public Roles() { }

    public static final String USER = "USER";
    public static final String ADM = "ADM";
  }
}
