package com.github.throyer.example.api.utils;

import static java.lang.String.format;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

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