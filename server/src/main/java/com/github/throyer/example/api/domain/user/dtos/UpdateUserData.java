package com.github.throyer.example.api.domain.user.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserData {
  @Schema(example = "Jubileu da Silva Sauro")
  @NotEmpty(message = "Name is a required field")
  private String name;
}
