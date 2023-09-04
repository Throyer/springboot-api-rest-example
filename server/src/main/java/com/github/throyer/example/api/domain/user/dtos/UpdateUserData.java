package com.github.throyer.example.api.domain.user.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserData {
  @Schema(example = "Jubileu da Silva Sauro")
  @NotEmpty(message = "{field.name.required}")
  @Size(min = 1, max = 100, message = "{field.name.invalid-size}")
  private String name;
}
