package com.github.throyer.common.springboot.utils;

import static com.github.throyer.common.springboot.constants.SECURITY.JWT;
import static com.github.throyer.common.springboot.constants.SECURITY.TOKEN_SECRET;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.List.of;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.github.javafaker.Faker;
import com.github.throyer.common.springboot.domain.role.entity.Role;
import com.github.throyer.common.springboot.domain.user.entity.User;

public class Random {
  private Random() {
  }

  private static final java.util.Random JAVA_RANDOM = new java.util.Random();
  public static final Faker FAKER = new Faker(new Locale("pt", "BR"));

  public static Integer between(Integer min, Integer max) {
    return JAVA_RANDOM.nextInt(max - min) + min;
  }

  public static <T> T element(List<T> list) {
    return list.get(JAVA_RANDOM.nextInt(list.size()));
  }

  public static String code() {
    return format("%s%s%s%s", between(0, 9), between(0, 9), between(0, 9), between(0, 9));
  }

  public static String name() {
    return FAKER.name().fullName();
  }

  public static String email() {
    return FAKER.internet().safeEmailAddress();
  }

  public static String password() {
    return FAKER.regexify("[a-z]{5,13}[1-9]{1,5}[A-Z]{1,5}[#?!@$%^&*-]{1,5}");
  }

  public static List<User> users(Integer size) {
    List<User> users = new ArrayList<>();
    for (int index = 0; index < size; index++) {
      users.add(user());
    }
    return users;
  }

  public static User user() {
    return user(of());
  }

  public static User user(List<Role> roles) {
    return new User(
        name(),
        email(),
        password(),
        roles);
  }

  public static String token(Long id, String roles) {
    return token(id, now().plusHours(24), TOKEN_SECRET, of(roles.split(",")));
  }

  public static String token(String roles) {
    return token(between(1, 9999).longValue(), now().plusHours(24), TOKEN_SECRET, of(roles.split(",")));
  }

  public static String token(String roles, String secret) {
    return token(between(1, 9999).longValue(), now().plusHours(24), secret, of(roles.split(",")));
  }

  public static String token(LocalDateTime expiration, String roles) {
    return token(between(1, 9999).longValue(), expiration, TOKEN_SECRET, of(roles.split(",")));
  }

  public static String token(LocalDateTime expiration, String roles, String secret) {
    return token(between(1, 9999).longValue(), expiration, secret, of(roles.split(",")));
  }

  public static String token(Long id, LocalDateTime expiration, String secret, List<String> roles) {
    var token = JWT.encode(id, roles, expiration, secret);
    return format("Bearer %s", token);
  }
}
