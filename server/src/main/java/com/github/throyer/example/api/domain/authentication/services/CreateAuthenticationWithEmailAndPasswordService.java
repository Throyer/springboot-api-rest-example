package com.github.throyer.example.api.domain.authentication.services;

import com.github.throyer.example.api.domain.authentication.dtos.CreateAuthenticationWithEmailAndPassword;
import com.github.throyer.example.api.domain.authentication.models.Authentication;
import com.github.throyer.example.api.domain.authentication.persistence.models.RefreshToken;
import com.github.throyer.example.api.domain.authentication.persistence.repositories.RefreshTokenRepository;
import com.github.throyer.example.api.domain.user.dtos.UserInformation;
import com.github.throyer.example.api.domain.user.persistence.repositories.UserRepository;
import com.github.throyer.example.api.infra.environments.SecurityProperties;
import com.github.throyer.example.api.shared.i18n.Internationalization;
import com.github.throyer.example.api.shared.jwt.JWT;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.github.throyer.example.api.shared.i18n.Internationalization.CREATE_OR_REFRESH_EMAIL_WAS_NOT_CONFIRMED;
import static com.github.throyer.example.api.shared.i18n.Internationalization.CREATE_SESSION_USERNAME_OR_PASSWORD_MESSAGE;
import static com.github.throyer.example.api.shared.rest.Responses.forbidden;
import static com.github.throyer.example.api.utils.ID.encode;
import static java.time.LocalDateTime.now;

@Slf4j
@Service
@AllArgsConstructor
public class CreateAuthenticationWithEmailAndPasswordService {
  private final Internationalization i18n;
  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;
  private final SecurityProperties properties;
  private final JWT jwt;
  
  public Authentication create(CreateAuthenticationWithEmailAndPassword body) {
    var email = body.getEmail();
    var password = body.getPassword();
    log.info("creating new session with username and password.");
    
    var user = userRepository.findOptionalByEmail(email)
        .orElseThrow(() -> {
          log.info("could not create session, user not find.");
          return forbidden(i18n.message(CREATE_SESSION_USERNAME_OR_PASSWORD_MESSAGE));
        });
    
    if (!user.passwordMatches(password)) {
      log.info("could not create session, invalid password.");
      throw forbidden(i18n.message(CREATE_SESSION_USERNAME_OR_PASSWORD_MESSAGE));
    }
    
    if (!user.emailHasConfirmed()) {
      log.info("could not create session, email was not confirmed.");
      throw forbidden(i18n.message(CREATE_OR_REFRESH_EMAIL_WAS_NOT_CONFIRMED));
    }
    
    var now = now();
    var expiresAt = now.plusHours(properties.getTokenExpirationInHours());

    var accessToken = jwt.encode(
      encode(user.getId()),
      user.getAuthorities(),
      expiresAt,
      properties.getTokenSecret()
    );

    log.info("creating new refresh-token for session.");
    
    refreshTokenRepository.disableOldRefreshTokensFromUser(user.getId());
    var refreshToken = new RefreshToken(user, properties.getRefreshTokenExpirationInDays());    
    refreshTokenRepository.save(refreshToken);
    
    log.info("refresh-token successfully created.");

    var auth = new Authentication(
      new UserInformation(user),
      accessToken,
      refreshToken.getCode(),
      expiresAt
    );

    log.info("session successfully created.");
    return auth;
  }
}
