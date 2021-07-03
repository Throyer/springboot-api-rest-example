package com.github.throyer.common.springboot.api.domain.models.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {
    
    private String token;
    private String refresh;
    private Long expiresIn;
    private String type;

    public Token(String token, Long expiresIn, String type) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.type = type;
    }

    @JsonProperty("access_token")
    public String getToken() {
        return token;
    }

    @JsonProperty("refresh_token")
    public String getRefresh() {
        return refresh;
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