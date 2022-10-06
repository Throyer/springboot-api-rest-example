package com.github.throyer.example.modules.authentication.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAuthenticationWithEmailAndPassword {
  @Schema(example = "jubileu@email.com", required = true)
  @NotBlank(message = "o campo email é obrigatório")
  @Email(message = "email invalido")
  private String email;
  
  @Schema(example = "veryStrongAndSecurePassword", required = true)
  @NotBlank(message = "o campo password é obrigatório")
  private String password;
}
