package com.github.throyer.common.springboot.api.domain.models.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {

    @JsonProperty("access_token")
    private String token;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("token_type")
    private String type;

    public Token(String token, Long expiresIn, String type) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    @JsonIgnore()
    public String getTokenType() {
        return type;
    }
}