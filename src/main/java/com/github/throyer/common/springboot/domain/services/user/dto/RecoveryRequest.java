package com.github.throyer.common.springboot.domain.services.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RecoveryRequest {

    @Email
    @NotNull
    @NotEmpty
    private String email;
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
