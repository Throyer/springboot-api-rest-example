package com.github.throyer.common.springboot.api.domain.security;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class Login {

    @NotNull(message = "O Email não pode ser NULO.")
    @NotEmpty(message = "Email invalido.")
    private String email;

    @NotNull(message = "A Senha não pode ser NULA.")
    @NotEmpty(message = "Senha invalida.")
    private String password;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UsernamePasswordAuthenticationToken toAuthenticationToken() {
        return new UsernamePasswordAuthenticationToken(this.email, this.password);
    }
}