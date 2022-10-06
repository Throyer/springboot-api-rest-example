package com.github.throyer.example.modules.users.dtos;

import static com.github.throyer.example.modules.mail.validations.EmailValidations.validateEmailUniqueness;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.validation.BindingResult;

import com.github.throyer.example.modules.mail.models.Addressable;
import com.github.throyer.example.modules.users.entities.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateUserProps implements Addressable {

  @NotEmpty(message = "${user.name.not-empty}")
  private String name;

  @NotEmpty(message = "{user.email.not-empty}")
  @Email(message = "{user.email.is-valid}")
  private String email;

  @NotEmpty(message = "{user.password.not-empty}")
  @Size(min = 8, max = 155, message = "{user.password.size}")
  private String password;

  public void validate(BindingResult result) {
    validateEmailUniqueness(this, result);
  }

  public void validate() {
    validateEmailUniqueness(this);
  }

  public User user() {
    return new User(name, email, password, List.of());
  }
}
