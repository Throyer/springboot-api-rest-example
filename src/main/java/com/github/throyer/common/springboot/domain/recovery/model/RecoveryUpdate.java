package com.github.throyer.common.springboot.domain.recovery.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RecoveryUpdate {
    @Email(message = "{recovery.email.is-valid}")
    @NotEmpty(message = "{recovery.email.not-empty}")
    private String email;

    @NotEmpty(message = "{recovery.code.not-empty}")
    private String code;

    @NotEmpty(message = "{user.password.not-empty}")
    @Size(min = 8, max = 155, message = "{user.password.size}")
    private String password;
}
