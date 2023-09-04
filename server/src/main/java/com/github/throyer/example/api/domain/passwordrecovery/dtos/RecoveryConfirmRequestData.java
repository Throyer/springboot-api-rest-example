package com.github.throyer.example.api.domain.passwordrecovery.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecoveryConfirmRequestData {
  @Schema(example = "jubileu@email.com")
  @Email(message = "{field.email.required}")
  @NotEmpty(message = "{field.email.invalid}")
  private String email;

  @Schema(example = "5894")
  @NotEmpty(message = "{field.recovery-code.required}")
  @Pattern(regexp = "[0-9][0-9][0-9][0-9]", message = "{field.recovery-code.invalid-code}")
  private String code;
}
