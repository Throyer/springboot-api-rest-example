package com.github.throyer.example.api.utils;

import com.github.javafaker.Faker;
import com.github.throyer.example.api.domain.role.persistence.models.Role;
import com.github.throyer.example.api.domain.user.persistence.models.User;
import com.github.throyer.example.api.infra.environments.SecurityProperties;
import com.github.throyer.example.api.shared.jwt.JsonWebTokenImplementation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.List.of;

@Component
public class Random {  
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
}