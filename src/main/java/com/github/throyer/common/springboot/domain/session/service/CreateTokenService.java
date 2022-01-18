package com.github.throyer.common.springboot.domain.session.service;


import com.github.throyer.common.springboot.domain.user.model.UserDetails;
import com.github.throyer.common.springboot.domain.user.repository.UserRepository;
import com.github.throyer.common.springboot.domain.session.entity.RefreshToken;
import com.github.throyer.common.springboot.domain.session.repository.RefreshTokenRepository;
import com.github.throyer.common.springboot.domain.session.model.TokenRequest;
import com.github.throyer.common.springboot.domain.session.model.TokenResponse;

import static com.github.throyer.common.springboot.utils.Constants.SECURITY.*;
import static com.github.throyer.common.springboot.utils.Responses.forbidden;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CreateTokenService {

    private final String TOKEN_SECRET;
    private final Integer TOKEN_EXPIRATION_IN_HOURS;
    private final Integer REFRESH_TOKEN_EXPIRATION_IN_DAYS;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public CreateTokenService(
        @Value(TOKEN_SECRET_ENV_PROPERTY) String tokenSecret,
        @Value(EXPIRATION_TOKEN_ENV_PROPERTY) Integer tokenExpirationInHours,
        @Value(REFRESH_TOKEN_ENV_PROPERTY) Integer refreshTokenExpirationInDays,
        RefreshTokenRepository refreshTokenRepository,
        UserRepository userRepository
    ) {
        this.TOKEN_SECRET = tokenSecret;
        this.TOKEN_EXPIRATION_IN_HOURS = tokenExpirationInHours;
        this.REFRESH_TOKEN_EXPIRATION_IN_DAYS = refreshTokenExpirationInDays;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public TokenResponse create(TokenRequest request) {
        var user = userRepository.findOptionalByEmailFetchRoles(request.getEmail())
            .filter(session -> session.validatePassword(request.getPassword()))
                .orElseThrow(() -> forbidden(CREATE_SESSION_ERROR_MESSAGE));
        return create(new UserDetails(user));
    }

    public TokenResponse create(UserDetails user) {

        var now = LocalDateTime.now();
        var expiresIn = now.plusHours(TOKEN_EXPIRATION_IN_HOURS);

        var token = JWT.encode(user, expiresIn, TOKEN_SECRET);
        var refresh = new RefreshToken(user, REFRESH_TOKEN_EXPIRATION_IN_DAYS);

        refreshTokenRepository.disableOldRefreshTokens(user.getId());

        refreshTokenRepository.save(refresh);

        return new TokenResponse(
            user,
            token,
            refresh,
            expiresIn
        );
    }
}