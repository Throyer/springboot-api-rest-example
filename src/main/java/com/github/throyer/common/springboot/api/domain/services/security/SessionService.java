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
import com.github.throyer.common.springboot.api.domain.services.security.dto.RefreshSessionRequest;
import com.github.throyer.common.springboot.api.domain.services.security.dto.RefreshSessionResponse;
import com.github.throyer.common.springboot.api.domain.services.security.dto.SessionRequest;
import com.github.throyer.common.springboot.api.domain.services.security.dto.SessionResponse;

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

    public ResponseEntity<SessionResponse> create(SessionRequest request) {
        var user = userRepository.findOptionalByEmail(request.getEmail())
                .filter(session -> session.validatePassword(request.getPassword()))
                    .orElseThrow(() -> forbidden(CREATE_SESSION_ERROR_MESSAGE));

        var now = LocalDateTime.now();
        
        var token = JWT.encode(user, now.plusHours(TOKEN_EXPIRATION_IN_HOURS), TOKEN_SECRET);
        var refresh = new RefreshToken(user, REFRESH_TOKEN_EXPIRATION_IN_DAYS);

        refreshTokenRepository.disableOldRefreshTokens(user.getId());
        
        refreshTokenRepository.save(refresh);

        var response = new SessionResponse(
            user,
            token,
            refresh,
            TOKEN_EXPIRATION_IN_HOURS
        );
        
        return ok(response);
    }

    public ResponseEntity<RefreshSessionResponse> refresh(RefreshSessionRequest request) {
        var old = refreshTokenRepository.findOptionalByCodeAndAvailableIsTrue(request.getRefresh())
            .filter(token -> token.expired())
            .orElseThrow(() -> forbidden(REFRESH_SESSION_ERROR_MESSAGE));
        
        var now = LocalDateTime.now();
        
        var token = JWT.encode(old.getUser(), now.plusHours(TOKEN_EXPIRATION_IN_HOURS), TOKEN_SECRET);
        
        refreshTokenRepository.disableOldRefreshTokens(old.getUser().getId());

        var refresh = refreshTokenRepository.save(new RefreshToken(old.getUser(), REFRESH_TOKEN_EXPIRATION_IN_DAYS));

        var response = new RefreshSessionResponse(
            token,
            refresh,
            TOKEN_EXPIRATION_IN_HOURS
        );
        
        return ok(response);
    }
}
