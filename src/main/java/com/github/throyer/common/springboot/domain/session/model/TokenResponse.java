package com.github.throyer.common.springboot.domain.session.model;

import com.github.throyer.common.springboot.domain.session.entity.RefreshToken;
import com.github.throyer.common.springboot.domain.user.entity.User;
import com.github.throyer.common.springboot.domain.user.model.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "Token", requiredProperties = {"user", "accessToken", "refreshToken", "expiresIn", "tokenType"})
public class TokenResponse {

    private final UserDetails user;
    private final String token;
    private final RefreshToken refreshToken;
    private final LocalDateTime expiresIn;

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
    
    public TokenResponse(
        UserDetails user,
        String token,
        RefreshToken refreshToken,
        LocalDateTime expiresIn
    ) {
        this.user = user;
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }

    public UserDetails getUser() {
        return user;
    }

    @JsonProperty("accessToken")
    public String getToken() {
        return token;
    }

    @JsonProperty("refreshToken")
    public String getRefresh() {
        return refreshToken.getCode();
    }

    @JsonFormat(shape = Shape.STRING)
    @JsonProperty("expiresIn")
    public LocalDateTime getExpiresIn() {
        return expiresIn;
    }

    @JsonProperty("tokenType")
    public String getTokenType() {
        return "Bearer";
    }
}
