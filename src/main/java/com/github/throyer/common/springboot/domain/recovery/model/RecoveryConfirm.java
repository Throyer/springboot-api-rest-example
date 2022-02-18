package com.github.throyer.common.springboot.domain.recovery.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class RecoveryConfirm {
    
    @Email(message = "{recovery.email.is-valid}")
    @NotEmpty(message = "{recovery.email.not-empty}")
    private String email;

    @NotEmpty(message = "{recovery.code.not-empty}")
    private String code;
}
