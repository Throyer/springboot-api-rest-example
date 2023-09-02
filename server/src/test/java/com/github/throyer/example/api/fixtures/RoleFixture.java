package com.github.throyer.example.api.fixtures;

import java.util.List;

import com.github.throyer.example.api.domain.role.persistence.models.Role;

public class RoleFixture {
  public static List<Role> roles() {
    return List.of(
      new Role(1L, "USER"),
      new Role(2L, "ADM")
    );
  }
}