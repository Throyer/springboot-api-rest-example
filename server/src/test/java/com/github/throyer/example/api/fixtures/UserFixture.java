package com.github.throyer.example.api.fixtures;

import com.github.throyer.example.api.domain.role.persistence.models.Role;
import com.github.throyer.example.api.domain.user.persistence.models.User;

import java.util.ArrayList;
import java.util.List;

import static com.github.throyer.example.api.fixtures.RoleFixture.roles;
import static com.github.throyer.example.api.utils.Random.*;


public class UserFixture {
  public static Long id() {
    return between(1, 999).longValue();
  }

  public static String name() {
    return faker().name().fullName();
  }

  public static String email() {
    return faker().internet().safeEmailAddress();
  }

  public static String password() {
    return faker().regexify("[a-z]{5,13}[1-9]{1,5}[A-Z]{1,5}[#?!@$%^&*-]{1,5}");
  }

  public static List<User> users(Integer size) {
    List<User> users = new ArrayList<>();
    for (int index = 0; index < size; index++) {
      users.add(user());
    }
    return users;
  }

  public static User user() {
    return user(List.of(element(roles())));
  }

  public static User user(Boolean emailConfirmed) {
    return new User(id(), name(), email(), password(), emailConfirmed, List.of(element(roles())));
  }

  public static User user(List<Role> roles) {
    return new User(id(), name(), email(), password(), true, roles);
  }

  public static User user(Long id, String name, String email, String password, List<Role> roles) {
    return new User(id, name, email, password, true, roles);
  }
}
