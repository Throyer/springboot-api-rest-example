package com.github.throyer.example.api.domain.passwordrecovery.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecoveryUpdatePasswordRequestData {
  @Schema(example = "jubileu@email.com")
  @Email(message = "{field.email.invalid}")
  @NotEmpty(message = "{field.email.required}")
  private String email;

  @Schema(example = "5894")
  @NotEmpty(message = "{field.recovery-code.required}")
  @Pattern(regexp = "[0-9][0-9][0-9][0-9]", message = "{field.recovery-code.invalid-code}")
  private String code;

  @Schema(example = "veryStrongAndSecurePassword")
  @NotEmpty(message = "{field.password.required}")
  @Size(min = 8, max = 155, message = "{field.password.invalid-size}")
  private String password;
}
