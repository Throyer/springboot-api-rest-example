package com.github.throyer.common.springboot.domain.services.user.dto;

import static com.github.throyer.common.springboot.domain.validation.EmailValidations.validateEmailUniqueness;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.github.throyer.common.springboot.domain.builders.UserBuilder;
import com.github.throyer.common.springboot.domain.models.entity.User;
import com.github.throyer.common.springboot.domain.models.shared.HasEmail;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserApi implements HasEmail {

    @NotEmpty(message = "email is a required field.")
    private String name;

    @NotEmpty(message = "email is a required field.")
    @Email(message = "invalid email.")
    private String email;

    @NotEmpty(message = "password is a required field.")
    @Size(min = 8, max = 155, message = "The password must contain at least {min} characters.")
    private String password;

    public CreateUserApi(String name, String email, String password) {
        setName(name);
        setEmail(email);
        setPassword(password);
    }

    public void validate() {
        validateEmailUniqueness(this);
    }
    
    public User user() {
        return new UserBuilder(name)
            .setEmail(email)
                .setPassword(password)
                    .build();
    }
}
