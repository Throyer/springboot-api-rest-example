package com.github.throyer.common.springboot.api.domain.services.security;

import static com.github.throyer.common.springboot.api.utils.Constants.SECURITY.CREATE_SESSION_ERROR_MESSAGE;
import static com.github.throyer.common.springboot.api.utils.Constants.SECURITY.REFRESH_SESSION_ERROR_MESSAGE;
import static com.github.throyer.common.springboot.api.utils.Constants.SECURITY.JWT;
import static com.github.throyer.common.springboot.api.utils.Responses.ok;
import static com.github.throyer.common.springboot.api.utils.Responses.forbidden;

import java.time.LocalDateTime;

import com.github.throyer.common.springboot.api.domain.models.entity.RefreshToken;
import com.github.throyer.common.springboot.api.domain.repositories.RefreshTokenRepository;
import com.github.throyer.common.springboot.api.domain.repositories.UserRepository;
import com.github.throyer.common.springboot.api.domain.services.security.dto.RefreshTokenRequest;
import com.github.throyer.common.springboot.api.domain.services.security.dto.RefreshTokenResponse;
import com.github.throyer.common.springboot.api.domain.services.security.dto.TokenRequest;
import com.github.throyer.common.springboot.api.domain.services.security.dto.TokenResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    @Value("${token.secret}")
    private String TOKEN_SECRET;

    @Value("${token.expiration-in-hours}")
    private Integer TOKEN_EXPIRATION_IN_HOURS;

    @Value("${token.refresh.expiration-in-days}")
    private Integer REFRESH_TOKEN_EXPIRATION_IN_DAYS;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public ResponseEntity<TokenResponse> create(TokenRequest request) {
        var user = userRepository.findOptionalByEmailFetchRoles(request.getEmail())
                .filter(session -> session.validatePassword(request.getPassword()))
                    .orElseThrow(() -> forbidden(CREATE_SESSION_ERROR_MESSAGE));

        var now = LocalDateTime.now();
        var expiresIn = now.plusHours(TOKEN_EXPIRATION_IN_HOURS);

        var token = JWT.encode(user, expiresIn, TOKEN_SECRET);
        var refresh = new RefreshToken(user, REFRESH_TOKEN_EXPIRATION_IN_DAYS);

        refreshTokenRepository.disableOldRefreshTokens(user.getId());
        
        refreshTokenRepository.save(refresh);

        var response = new TokenResponse(
            user,
            token,
            refresh,
            expiresIn
        );
        
        return ok(response);
    }

    public ResponseEntity<RefreshTokenResponse> refresh(RefreshTokenRequest request) {
        var old = refreshTokenRepository.findOptionalByCodeAndAvailableIsTrue(request.getRefresh())
            .filter(token -> token.nonExpired())
            .orElseThrow(() -> forbidden(REFRESH_SESSION_ERROR_MESSAGE));
        
        var now = LocalDateTime.now();
        var expiresIn = now.plusHours(TOKEN_EXPIRATION_IN_HOURS);
        var token = JWT.encode(old.getUser(), expiresIn, TOKEN_SECRET);
        
        refreshTokenRepository.disableOldRefreshTokens(old.getUser().getId());

        var refresh = refreshTokenRepository.save(new RefreshToken(old.getUser(), REFRESH_TOKEN_EXPIRATION_IN_DAYS));

        var response = new RefreshTokenResponse(
            token,
            refresh,
            expiresIn
        );
        
        return ok(response);
    }
}
