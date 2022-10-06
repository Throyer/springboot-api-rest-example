package com.github.throyer.example.modules.authentication.services;

import static com.github.throyer.example.modules.infra.constants.MessagesConstants.CREATE_SESSION_ERROR_MESSAGE;
import static com.github.throyer.example.modules.infra.constants.MessagesConstants.REFRESH_SESSION_ERROR_MESSAGE;
import static com.github.throyer.example.modules.infra.environments.SecurityEnvironments.JWT;
import static com.github.throyer.example.modules.infra.environments.SecurityEnvironments.REFRESH_TOKEN_EXPIRATION_IN_DAYS;
import static com.github.throyer.example.modules.infra.environments.SecurityEnvironments.TOKEN_EXPIRATION_IN_HOURS;
import static com.github.throyer.example.modules.infra.environments.SecurityEnvironments.TOKEN_SECRET;
import static com.github.throyer.example.modules.infra.http.Responses.forbidden;
import static com.github.throyer.example.modules.shared.utils.HashIdsUtils.encode;
import static com.github.throyer.example.modules.shared.utils.InternationalizationUtils.message;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.throyer.example.modules.authentication.dtos.CreateAuthenticationWithEmailAndPassword;
import com.github.throyer.example.modules.authentication.dtos.CreateAuthenticationWithRefreshToken;
import com.github.throyer.example.modules.authentication.entities.RefreshToken;
import com.github.throyer.example.modules.authentication.models.Authentication;
import com.github.throyer.example.modules.authentication.repositories.RefreshTokenRepository;
import com.github.throyer.example.modules.users.dtos.UserInformation;
import com.github.throyer.example.modules.users.repositories.UserRepository;

@Service
public class CreateAuthenticationService {
  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;

  @Autowired
  public CreateAuthenticationService(
    RefreshTokenRepository refreshTokenRepository,
    UserRepository userRepository
  ) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.userRepository = userRepository;
  }
  
  public Authentication create(CreateAuthenticationWithEmailAndPassword body) {
    var user = userRepository.findByEmail(body.getEmail())
      .filter(databaseUser -> databaseUser.validatePassword(body.getPassword()))
        .orElseThrow(() -> forbidden(message(CREATE_SESSION_ERROR_MESSAGE)));
    
    var now = LocalDateTime.now();
    var expiresAt = now.plusHours(TOKEN_EXPIRATION_IN_HOURS);

    var accessToken = JWT.encode(
      encode(user.getId()),
      user.getAuthorities(),
      expiresAt,
      TOKEN_SECRET
    );

    var refreshToken = new RefreshToken(
      user,
      REFRESH_TOKEN_EXPIRATION_IN_DAYS
    );

    refreshTokenRepository.disableOldRefreshTokens(user.getId());

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
        .orElseThrow(() -> forbidden(message(REFRESH_SESSION_ERROR_MESSAGE)));

    var user = old.getUser();

    var now = LocalDateTime.now();
    var expiresAt = now.plusHours(TOKEN_EXPIRATION_IN_HOURS);

    var accessToken = JWT.encode(
      encode(user.getId()),
      user.getAuthorities(),
      expiresAt,
      TOKEN_SECRET
    );

    refreshTokenRepository.disableOldRefreshTokens(user.getId());

    var refreshToken = new RefreshToken(user, TOKEN_EXPIRATION_IN_HOURS);

    refreshTokenRepository.save(refreshToken);

    return new Authentication(
      new UserInformation(user),
      accessToken,
      refreshToken.getCode(),
      expiresAt
    );
  }
}
