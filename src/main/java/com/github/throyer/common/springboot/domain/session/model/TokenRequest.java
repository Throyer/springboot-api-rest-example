package com.github.throyer.common.springboot.domain.session.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class TokenRequest {

    @NotEmpty(message = "{token.email.not-null}")
    @Email(message = "{token.email.is-valid}")
    private String email;

    @NotEmpty(message = "{token.password.not-null}")
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenRequest that)) return false;
        return getEmail().equals(that.getEmail()) && getPassword().equals(that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getPassword());
    }
}
