package com.github.throyer.common.springboot.api.domain.services.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.github.throyer.common.springboot.api.domain.models.shared.HasEmail;

public class UpdateUser implements HasEmail {
    
    @NotNull(message = "O nome não pode ser NULL.")
    @NotEmpty(message = "Por favor, forneça um nome.")
    private String name;
    
    @NotNull(message = "O e-mail não pode ser NULL.")
    @Email(message = "Por favor, forneça um e-mail valido.")
    private String email;
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
