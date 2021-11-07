package com.github.throyer.common.springboot.domain.services.security.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.throyer.common.springboot.domain.models.entity.RefreshToken;
import com.github.throyer.common.springboot.domain.models.entity.User;
import com.github.throyer.common.springboot.domain.services.user.dto.UserDetails;

public class TokenResponse {    
    private final UserDetails user;
    private final String token;
    private final RefreshToken refreshToken;
    private final LocalDateTime expiresIn;
    private final String type = "Bearer";
    
    public TokenResponse(
        User user,
        String token,
        RefreshToken refreshToken,
        LocalDateTime expiresIn
    ) {
        this.user = new UserDetails(user);
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }

    public UserDetails getUser() {
        return user;
    }

    @JsonProperty("access_token")
    public String getToken() {
        return token;
    }

    @JsonProperty("refresh_token")
    public String getRefresh() {
        return refreshToken.getCode();
    }

    @JsonFormat(shape = Shape.STRING)
    @JsonProperty("expires_in")
    public LocalDateTime getExpiresIn() {
        return expiresIn;
    }

    @JsonProperty("token_type")
    public String getTokenType() {
        return type;
    }
}
