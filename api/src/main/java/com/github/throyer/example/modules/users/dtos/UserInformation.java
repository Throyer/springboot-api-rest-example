package com.github.throyer.example.modules.users.dtos;

import java.util.List;

import com.github.throyer.example.modules.shared.utils.HashIdsUtils;
import com.github.throyer.example.modules.users.entities.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserInformation {

  @Schema(example = "BVnl07r1Joz3", required = true)
  private final String id;

  @Schema(example = "Jubileu da Silva", required = true)
  private final String name;

  @Schema(example = "jubileu@email.com", required = true)
  private final String email;

  @Schema(example = "true", required = true)
  private Boolean active;

  @Schema(example = "[\"ADM\"]", required = true)
  private final List<String> roles;

  public UserInformation(User user) {
    this.id = HashIdsUtils.encode(user.getId());
    this.name = user.getName();
    this.email = user.getEmail();
    this.active = user.isActive();
    this.roles = user.getAuthorities();
  }
}
