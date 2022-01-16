package com.github.throyer.common.springboot.domain.session.model;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenRequest {

    @NotEmpty(message = "Invalid refresh_token.")
    @JsonProperty("refresh_token")
    private String refresh;

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }
}
