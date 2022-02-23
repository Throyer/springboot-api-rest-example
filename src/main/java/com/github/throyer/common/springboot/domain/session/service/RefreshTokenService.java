package com.github.throyer.common.springboot.domain.session.service;

import com.github.throyer.common.springboot.domain.session.entity.RefreshToken;
import com.github.throyer.common.springboot.domain.session.model.RefreshTokenRequest;
import com.github.throyer.common.springboot.domain.session.model.RefreshTokenResponse;
import com.github.throyer.common.springboot.domain.session.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.throyer.common.springboot.constants.MESSAGES.REFRESH_SESSION_ERROR_MESSAGE;
import static com.github.throyer.common.springboot.constants.SECURITY.*;
import static com.github.throyer.common.springboot.utils.Messages.message;
import static com.github.throyer.common.springboot.utils.Responses.forbidden;
import static java.time.LocalDateTime.now;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshTokenResponse refresh(RefreshTokenRequest request) {
        var old = refreshTokenRepository.findOptionalByCodeAndAvailableIsTrue(request.getRefresh())
            .filter(RefreshToken::nonExpired)
                .orElseThrow(() -> forbidden(message(REFRESH_SESSION_ERROR_MESSAGE)));

        var now = now();
        var expiresIn = now.plusHours(TOKEN_EXPIRATION_IN_HOURS);
        var token = JWT.encode(old.getUser(), expiresIn, TOKEN_SECRET);

        refreshTokenRepository.disableOldRefreshTokens(old.getUser().getId());

        var refresh = refreshTokenRepository.save(new RefreshToken(old.getUser(), REFRESH_TOKEN_EXPIRATION_IN_DAYS));

        return new RefreshTokenResponse(
            token,
            refresh,
            expiresIn
        );
    }
}