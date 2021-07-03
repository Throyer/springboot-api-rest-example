package com.github.throyer.common.springboot.api.domain.services.security.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshSessionRequest {

    @NotNull(message = "refresh_token não pode NULO.")
    @NotEmpty(message = "refresh_token invalido.")
    @JsonProperty("refresh_token")
    private String refresh;

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }
}
