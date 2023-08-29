package com.github.throyer.example.api.domain.authentication.services;

import com.github.throyer.example.api.domain.authentication.dtos.CreateAuthenticationWithEmailAndPassword;
import com.github.throyer.example.api.domain.authentication.dtos.CreateAuthenticationWithRefreshToken;
import com.github.throyer.example.api.domain.authentication.models.Authentication;
import com.github.throyer.example.api.domain.authentication.persistence.models.RefreshToken;
import com.github.throyer.example.api.domain.authentication.persistence.repositories.RefreshTokenRepository;
import com.github.throyer.example.api.domain.user.dtos.UserInformation;
import com.github.throyer.example.api.domain.user.persistence.repositories.UserRepository;
import com.github.throyer.example.api.infra.environments.SecurityProperties;
import com.github.throyer.example.api.shared.jwt.JWT;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.github.throyer.example.api.shared.rest.Responses.forbidden;
import static com.github.throyer.example.api.utils.ID.encode;
import static java.time.LocalDateTime.now;

@Service
@AllArgsConstructor
public class CreateAuthenticationService {
  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;  
  private final SecurityProperties properties;
  private final JWT jwt;

  public Authentication create(CreateAuthenticationWithEmailAndPassword body) {
    var user = userRepository.findOptionalByEmail(body.getEmail())
      .filter(databaseUser -> databaseUser.validate(body.getPassword()))
        .orElseThrow(() -> forbidden("Invalid password or username."));

    var now = now();
    var expiresAt = now.plusHours(properties.getTokenExpirationInHours());

    var accessToken = jwt.encode(
      encode(user.getId()),
      user.getAuthorities(),
      expiresAt,
      properties.getTokenSecret()
    );

    var refreshToken = new RefreshToken(user, properties.getRefreshTokenExpirationInDays());

    refreshTokenRepository.disableOldRefreshTokensFromUser(user.getId());

    refreshTokenRepository.save(refreshToken);

    return new Authentication(
      new UserInformation(user),
      accessToken,
      refreshToken.getCode(),
      expiresAt
    );
  }

  public Authentication create(CreateAuthenticationWithRefreshToken body) {
    var old = refreshTokenRepository.findOptionalByCodeAndAvailableIsTrue(body.getRefreshToken())
      .filter(RefreshToken::nonExpired)
      .orElseThrow(() -> forbidden("Refresh token expired or invalid."));

    var user = old.getUser();

    var now = now();
    var expiresAt = now.plusHours(properties.getTokenExpirationInHours());

    var accessToken = jwt.encode(
      encode(user.getId()),
      user.getAuthorities(),
      expiresAt,
      properties.getTokenSecret()
    );

    refreshTokenRepository.disableOldRefreshTokensFromUser(user.getId());

    var refreshToken = new RefreshToken(user, properties.getRefreshTokenExpirationInDays());

    refreshTokenRepository.save(refreshToken);

    return new Authentication(
      new UserInformation(user),
      accessToken,
      refreshToken.getCode(),
      expiresAt
    );
  }
}
