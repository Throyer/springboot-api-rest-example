package com.github.throyer.example.api.domain.authentication.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAuthenticationWithEmailAndPassword {
  @Schema(example = "jubileu@email.com", requiredMode = REQUIRED)
  @NotBlank(message = "{field.email.required}")
  @Email(message = "{field.email.invalid}")
  private String email;

  @Schema(example = "veryStrongAndSecurePassword", requiredMode = REQUIRED)
  @NotBlank(message = "{field.password.required}")
  private String password;
}