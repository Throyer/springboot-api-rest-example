package com.github.throyer.example.api.domain.authentication.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Setter
public class CreateAuthenticationWithEmailAndPassword {
  @Schema(example = "jubileu@email.com", requiredMode = REQUIRED)
  @NotBlank(message = "o campo email é obrigatório")
  @Email(message = "email invalido")
  private String email;

  @Schema(example = "veryStrongAndSecurePassword", requiredMode = REQUIRED)
  @NotBlank(message = "o campo password é obrigatório")
  private String password;
}