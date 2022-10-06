package com.github.throyer.example.modules.users.dtos;

import static com.github.throyer.example.modules.mail.validations.EmailValidations.validateEmailUniqueness;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.validation.BindingResult;

import com.github.throyer.example.modules.mail.models.Addressable;
import com.github.throyer.example.modules.users.entities.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateUserProps implements Addressable {

  @Schema(example = "Jubileu da silva")
  @NotEmpty(message = "${user.name.not-empty}")
  private String name;
  
  @Schema(example = "jubileu@email.com")
  @NotEmpty(message = "{user.email.not-empty}")
  @Email(message = "{user.email.is-valid}")
  private String email;
  
  @Schema(example = "veryStrongAndSecurePassword")
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
