package com.github.throyer.example.api.domain.user.dtos;

import com.github.throyer.example.api.shared.mail.models.Addressable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserData implements Addressable {
  @Schema(example = "Jubileu da silva")
  @NotEmpty(message = "Name is a required field")
  private String name;

  @Schema(example = "jubileu@email.com")
  @NotEmpty(message = "Email is a required field.")
  @Email(message = "Email is not valid.")
  private String email;

  @Schema(example = "veryStrongAndSecurePassword")
  @NotEmpty(message = "Password is a required field.")
  @Size(min = 8, max = 155, message = "The password must contain at least {min} characters.")
  private String password;
}
