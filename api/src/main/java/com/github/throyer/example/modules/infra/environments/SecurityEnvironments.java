package com.github.throyer.example.modules.infra.environments;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.throyer.example.modules.authentication.services.JsonWebToken;

@Component
public class SecurityEnvironments {
  public static String TOKEN_SECRET;
  public static Integer TOKEN_EXPIRATION_IN_HOURS;
  public static Integer REFRESH_TOKEN_EXPIRATION_IN_DAYS;

  public static String HASHIDS_SECRET;

  public static String SESSION_COOKIE_NAME;
  public static String SESSION_COOKIE_SECRET;

  @Autowired
  public SecurityEnvironments(
    @Value("${token.secret}") String tokenSecret,
    @Value("${hashid.secret}") String hashidSecret,
    @Value("${cookie.secret}") String cookieSecret,
    @Value("${token.expiration-in-hours}") Integer tokenExpirationInHours,
    @Value("${token.refresh.expiration-in-days}") Integer refreshTokenExpirationInDays,
    @Value("${server.servlet.session.cookie.name}") String sessionCookieName
  ) {
    SecurityEnvironments.HASHIDS_SECRET = hashidSecret;
    SecurityEnvironments.TOKEN_SECRET = tokenSecret;
    SecurityEnvironments.SESSION_COOKIE_SECRET = cookieSecret;
    SecurityEnvironments.TOKEN_EXPIRATION_IN_HOURS = tokenExpirationInHours;
    SecurityEnvironments.REFRESH_TOKEN_EXPIRATION_IN_DAYS = refreshTokenExpirationInDays;
    SecurityEnvironments.SESSION_COOKIE_NAME = sessionCookieName;
  }

  public static final Integer DAY_MILLISECONDS = 86400;
  public static final JsonWebToken JWT = new JsonWebToken();
  
  public static final Integer HASH_LENGTH = 10;
  public static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(HASH_LENGTH);
  public static final Hashids HASH_ID = new Hashids(HASHIDS_SECRET, HASH_LENGTH);

  public static final String ROLES_KEY_ON_JWT = "roles";

  public static final String USERNAME_PARAMETER = "email";
  public static final String PASSWORD_PARAMETER = "password";

  public static final String HOME_URL = "/app";
  public static final String LOGIN_URL = "/app/login";
  public static final String LOGIN_ERROR_URL = LOGIN_URL + "?error=true";
  public static final String ACESSO_NEGADO_URL = LOGIN_URL + "?denied=true";
  public static final String LOGOUT_URL = "/app/logout";

  public static final String SECURITY_TYPE = "Bearer";
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String ACCEPTABLE_TOKEN_TYPE = SECURITY_TYPE + " ";
  public static final String CAN_T_WRITE_RESPONSE_ERROR = "can't write response error.";
  public static final Integer BEARER_WORD_LENGTH = SECURITY_TYPE.length();
}
