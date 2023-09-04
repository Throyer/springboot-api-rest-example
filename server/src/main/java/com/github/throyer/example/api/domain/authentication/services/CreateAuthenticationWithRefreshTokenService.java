package com.github.throyer.example.api.domain.authentication.services;

import com.github.throyer.example.api.domain.authentication.dtos.CreateAuthenticationWithRefreshToken;
import com.github.throyer.example.api.domain.authentication.models.Authentication;
import com.github.throyer.example.api.domain.authentication.persistence.models.RefreshToken;
import com.github.throyer.example.api.domain.authentication.persistence.repositories.RefreshTokenRepository;
import com.github.throyer.example.api.domain.user.dtos.UserInformation;
import com.github.throyer.example.api.infra.environments.SecurityProperties;
import com.github.throyer.example.api.shared.i18n.Internationalization;
import com.github.throyer.example.api.shared.jwt.JWT;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.github.throyer.example.api.infra.constants.MessageConstants.Authentication.EMAIL_WAS_NOT_CONFIRMED_MESSAGE;
import static com.github.throyer.example.api.infra.constants.MessageConstants.Authentication.RECOVERY_CODE_EXPIRED_OR_INVALID_MESSAGE;
import static com.github.throyer.example.api.shared.rest.Responses.forbidden;
import static com.github.throyer.example.api.utils.ID.encode;
import static java.time.LocalDateTime.now;

@Slf4j
@Service
@AllArgsConstructor
public class CreateAuthenticationWithRefreshTokenService {
  private final Internationalization i18n;
  private final RefreshTokenRepository refreshTokenRepository;
  private final SecurityProperties properties;
  private final JWT jwt;
  
  public Authentication create(CreateAuthenticationWithRefreshToken body) {
    log.info("creating new session refresh-token.");
    var currentCode = body.getRefreshToken();
    
    var old = refreshTokenRepository.findOptionalByCodeAndAvailableIsTrue(currentCode)
      .orElseThrow(() -> {
        log.info("could not create session, invalid refresh-token code.");
        return forbidden(i18n.message(RECOVERY_CODE_EXPIRED_OR_INVALID_MESSAGE));
      });
    
    if (old.isExpired()) {
      log.info("could not create session, expired refresh-token.");
      throw forbidden(i18n.message(RECOVERY_CODE_EXPIRED_OR_INVALID_MESSAGE));
    }
    
    var user = old.getUser();

    if (!user.emailHasConfirmed()) {
      log.info("could not create session, email was not confirmed.");
      throw forbidden(i18n.message(EMAIL_WAS_NOT_CONFIRMED_MESSAGE));
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
