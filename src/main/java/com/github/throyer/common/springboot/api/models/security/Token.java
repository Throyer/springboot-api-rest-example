package com.github.throyer.common.springboot.api.models.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {

    @JsonProperty("access_token")
    private String token;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("token_type")
    private String tipo;

    public Token(String token, Long expiresIn, String tipo) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.tipo = tipo;
    }

    public String getToken() {
        return token;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public String getTipo() {
        return tipo;
    }
}