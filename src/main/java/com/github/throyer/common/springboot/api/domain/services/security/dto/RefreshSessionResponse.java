package com.github.throyer.common.springboot.api.domain.services.security.dto;

import static com.github.throyer.common.springboot.api.utils.Constants.SECURITY.HOUR_IN_SECONDS;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.throyer.common.springboot.api.domain.models.entity.RefreshToken;

public class RefreshSessionResponse {
    private final String token;
    private final RefreshToken refreshToken;
    private final Long expiresIn;
    private final String type = "Bearer";

    public RefreshSessionResponse(
        String token,
        RefreshToken refreshToken,
        Integer tokenExpirationInHours
    ) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresIn = (tokenExpirationInHours * HOUR_IN_SECONDS);
    }

    @JsonProperty("access_token")
    public String getToken() {
        return token;
    }

    @JsonProperty("refresh_token")
    public String getRefresh() {
        return refreshToken.getCode();
    }

    @JsonProperty("expires_in")
    public Long getExpiresIn() {
        return expiresIn;
    }

    @JsonProperty("token_type")
    public String getTokenType() {
        return type;
    }
}
