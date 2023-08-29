package com.github.throyer.example.api.domain.role.dtos;

import com.github.throyer.example.api.domain.role.persistence.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import static com.github.throyer.example.api.utils.ID.encode;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
public class RoleInformation {
  @Schema(example = "A1PLgjPPlM8x", requiredMode = REQUIRED)
  private final String id;

  @Schema(example = "Administrador", requiredMode = REQUIRED)
  private final String name;

  @Schema(example = "ADM", requiredMode = REQUIRED)
  private final String shortName;

  @Schema(example = "Administrador do sistema", requiredMode = REQUIRED)
  private final String description;

  public RoleInformation(Role role) {
    this.id = encode(role.getId());
    this.name = role.getName();
    this.shortName = role.getShortName();
    this.description = role.getDescription();
  }
}