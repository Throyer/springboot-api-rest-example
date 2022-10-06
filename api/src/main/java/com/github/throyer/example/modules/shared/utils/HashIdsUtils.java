package com.github.throyer.example.modules.shared.utils;

import static com.github.throyer.example.modules.infra.environments.SecurityEnvironments.HASH_ID;

import java.util.Arrays;

public class HashIdsUtils {
  public static String encode(Long id) {
    return HASH_ID.encode(id);
  }

  public static Long decode(String id) {
    return Arrays.stream(HASH_ID.decode(id))
      .boxed()
        .findFirst()
          .orElse(null);
  }
}
