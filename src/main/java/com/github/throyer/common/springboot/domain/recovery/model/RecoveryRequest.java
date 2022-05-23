package com.github.throyer.common.springboot.domain.recovery.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecoveryRequest {

    @Email(message = "{recovery.email.is-valid}")
    @NotEmpty(message = "{recovery.email.not-empty}")
    private String email;
}
