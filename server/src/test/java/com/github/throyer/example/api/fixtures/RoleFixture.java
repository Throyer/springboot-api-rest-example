package com.github.throyer.example.api.fixtures;

import com.github.throyer.example.api.domain.role.persistence.models.Role;

import java.util.List;

public class RoleFixture {
  public static List<Role> roles() {
    return List.of(
      new Role(1L, "USER"),
      new Role(2L, "ADM")
    );
  }
}