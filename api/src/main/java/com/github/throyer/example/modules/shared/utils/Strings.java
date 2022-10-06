package com.github.throyer.example.modules.shared.utils;

import java.util.Objects;
import java.util.stream.Stream;

public class Strings {
  private Strings() { }
  
  public static Boolean notNullOrBlank(String string) {
    if (Objects.isNull(string)) {
      return false;
    }
    return !string.isBlank();
  }

  public static Boolean noneOfThenNullOrEmpty(String... strings) {
    return Stream.of(strings).allMatch(Strings::notNullOrBlank);
  }
}
