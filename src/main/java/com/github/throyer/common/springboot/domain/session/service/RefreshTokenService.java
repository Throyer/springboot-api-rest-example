package com.github.throyer.common.springboot.domain.session.service;

import com.github.throyer.common.springboot.domain.session.entity.RefreshToken;
import com.github.throyer.common.springboot.domain.session.model.RefreshTokenRequest;
import com.github.throyer.common.springboot.domain.session.model.RefreshTokenResponse;
import com.github.throyer.common.springboot.domain.session.repository.RefreshTokenRepository;
import static com.github.throyer.common.springboot.utils.Constants.SECURITY.JWT;
import static com.github.throyer.common.springboot.utils.Responses.forbidden;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {

    public static final String REFRESH_SESSION_ERROR_MESSAGE = "Refresh token expirado ou inválido.";

    @Value("${token.secret}")
    private String TOKEN_SECRET;

    @Value("${token.expiration-in-hours}")
    private Integer TOKEN_EXPIRATION_IN_HOURS;

    @Value("${token.refresh.expiration-in-days}")
    private Integer REFRESH_TOKEN_EXPIRATION_IN_DAYS;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenResponse refresh(RefreshTokenRequest request) {
        var old = refreshTokenRepository.findOptionalByCodeAndAvailableIsTrue(request.getRefresh())
            .filter(token -> token.nonExpired())
                .orElseThrow(() -> forbidden(REFRESH_SESSION_ERROR_MESSAGE));

        var now = LocalDateTime.now();
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