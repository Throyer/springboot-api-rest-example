package com.github.throyer.common.springboot.api.domain.services.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RecoveryConfirm {
    
    @Email
    @NotNull
    @NotEmpty
    private String email;

    @Email
    @NotNull
    @NotEmpty
    private String code;
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
