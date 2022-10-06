package com.github.throyer.example.modules.users.dtos;

import static com.github.throyer.example.modules.mail.validations.EmailValidations.validateEmailUniqueness;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.validation.BindingResult;

import com.github.throyer.example.modules.mail.models.Addressable;
import com.github.throyer.example.modules.roles.entities.Role;
import com.github.throyer.example.modules.shared.utils.JSON;
import com.github.throyer.example.modules.users.entities.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateOrUpdateUserByAppForm implements Addressable {

  private Long id;

  @NotEmpty(message = "${user.name.not-empty}")
  private String name;

  @NotEmpty(message = "{user.email.not-empty}")
  @Email(message = "{user.email.is-valid}")
  private String email;

  @NotNull
  private List<Role> roles;

  public void validate(BindingResult result) {
    validateEmailUniqueness(this, result);
  }

  public void validate() {
    validateEmailUniqueness(this);
  }

  public User user() {
    var user = new User(name, email, null, roles);
    user.setId(id);
    return user;
  }

  @Override
  public String toString() {
    return JSON.stringify(this);
  }
}
