package com.github.throyer.common.springboot.domain.services.user.dto;

import static com.github.throyer.common.springboot.domain.validation.EmailValidations.validateEmailUniqueness;

import com.github.throyer.common.springboot.domain.builders.UserBuilder;

import com.github.throyer.common.springboot.domain.models.entity.User;
import com.github.throyer.common.springboot.domain.models.shared.HasEmail;

import static com.github.throyer.common.springboot.domain.services.user.dto.CreateUserApi.STRONG_PASSWORD;
import static com.github.throyer.common.springboot.domain.services.user.dto.CreateUserApi.STRONG_PASSWORD_MESSAGE;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

import org.springframework.validation.BindingResult;

@Data
public class CreateUserApp implements HasEmail {

    @NotEmpty(message = "Por favor, forneça um nome.")
    private String name;

    @NotEmpty(message = "Por favor, forneça um e-mail.")
    @Email(message = "Por favor, forneça um e-mail valido.")
    private String email;

    @NotEmpty(message = "Por favor, forneça uma senha.")
    @Size(min = 8, max = 255, message = "A senha deve conter no minimo {min} caracteres.")
    private String password;

    public void validate(BindingResult result) {
        validateEmailUniqueness(this, result);
    }
    
    public User user() {
        return new UserBuilder(name)
            .setEmail(email)
                .setPassword(password)
                    .build();
    }
}
