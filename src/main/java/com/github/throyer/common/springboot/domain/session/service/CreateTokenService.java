package com.github.throyer.common.springboot.domain.session.service;


import com.github.throyer.common.springboot.domain.session.entity.RefreshToken;
import com.github.throyer.common.springboot.domain.session.model.TokenRequest;
import com.github.throyer.common.springboot.domain.session.model.TokenResponse;
import com.github.throyer.common.springboot.domain.session.repository.RefreshTokenRepository;
import com.github.throyer.common.springboot.domain.user.model.UserDetails;
import com.github.throyer.common.springboot.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.github.throyer.common.springboot.utils.Constants.MESSAGES.CREATE_SESSION_ERROR_MESSAGE;
import static com.github.throyer.common.springboot.utils.Constants.SECURITY.*;
import static com.github.throyer.common.springboot.utils.Messages.message;
import static com.github.throyer.common.springboot.utils.Responses.forbidden;

@Service
public class CreateTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public CreateTokenService(
        RefreshTokenRepository refreshTokenRepository,
        UserRepository userRepository
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public TokenResponse create(TokenRequest request) {
        var session = userRepository.findByEmail(request.getEmail())
            .filter(user -> user.validatePassword(request.getPassword()))
                .orElseThrow(() -> forbidden(message(CREATE_SESSION_ERROR_MESSAGE)));
        return create(new UserDetails(session));
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