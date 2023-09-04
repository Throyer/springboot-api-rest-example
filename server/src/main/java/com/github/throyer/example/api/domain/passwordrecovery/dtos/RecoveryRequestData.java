package com.github.throyer.example.api.domain.passwordrecovery.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecoveryRequestData {
  @Schema(example = "jubileu@email.com")
  @Email(message = "{field.email.invalid}")
  @NotEmpty(message = "{field.email.required}")
  private String email;
}
