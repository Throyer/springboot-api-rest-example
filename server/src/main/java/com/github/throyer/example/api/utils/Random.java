package com.github.throyer.example.api.utils;

import com.github.javafaker.Faker;

import java.util.List;
import java.util.Locale;

public class Random {  
  private static final java.util.Random JAVA_RANDOM = new java.util.Random();
  
  public static Integer between(Integer min, Integer max) {
    return JAVA_RANDOM.nextInt(max - min) + min;
  }

  public static <T> T element(List<T> list) {
    return list.get(JAVA_RANDOM.nextInt(list.size()));
  }
  
  public static Faker faker() {
    return new Faker(new Locale("pt", "BR"));
  }
  
  public static String code() {
    return faker().regexify("[0-9][0-9][0-9][0-9]");
  }
  
}