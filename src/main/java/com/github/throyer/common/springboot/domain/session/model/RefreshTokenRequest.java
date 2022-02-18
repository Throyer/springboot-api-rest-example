package com.github.throyer.common.springboot.domain.session.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class RefreshTokenRequest {

    @NotEmpty(message = "{token.refresh-token.not-null}")
    private String refresh;

    @JsonProperty("refreshToken")
    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }
}
