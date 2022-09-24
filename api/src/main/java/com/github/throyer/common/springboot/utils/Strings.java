package com.github.throyer.common.springboot.utils;

import java.util.Objects;

public class Strings {
  private Strings() {}

  public static Boolean notNullOrBlank(String string) {
    if (Objects.isNull(string)) {
      return false;
    }
    return !string.isBlank();
  }
}
