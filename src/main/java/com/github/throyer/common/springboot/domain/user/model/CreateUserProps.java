package com.github.throyer.common.springboot.domain.user.model;

import static com.github.throyer.common.springboot.domain.mail.exceptions.EmailValidations.validateEmailUniqueness;

import com.github.throyer.common.springboot.domain.management.model.Addressable;
import com.github.throyer.common.springboot.domain.user.entity.User;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateUserProps implements Addressable {

    @NotEmpty(message = "email is a required field.")
    private String name;

    @NotEmpty(message = "email is a required field.")
    @Email(message = "invalid email.")
    private String email;

    @NotEmpty(message = "password is a required field.")
    @Size(min = 8, max = 155, message = "The password must contain at least {min} characters.")
    private String password;

    public CreateUserProps(String name, String email, String password) {
        setName(name);
        setEmail(email);
        setPassword(password);
    }

    public void validate() {
        validateEmailUniqueness(this);
    }
    
    public User user() {
        return new User(name, email, password, List.of());
    }
}
