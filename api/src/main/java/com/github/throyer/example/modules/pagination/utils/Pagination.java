package com.github.throyer.example.modules.pagination.utils;

import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

public class Pagination {

  private static final int FIRST_PAGE = 0;
  private static final int DEFAULT_SIZE = 10;

  private static final int MIN_SIZE = 1;
  private static final int MAX_SIZE = 500;

  public static Pageable of(Optional<Integer> page, Optional<Integer> size) {
    return Pagination.of(page.orElse(FIRST_PAGE), size.orElse(DEFAULT_SIZE));
  }

  public static Pageable of(Integer page, Integer size) {
    if (page < FIRST_PAGE) {
      page = FIRST_PAGE;
    }

    if (size < MIN_SIZE || size > MAX_SIZE) {
      size = DEFAULT_SIZE;
    }
    return PageRequest.of(page, size);
  }
}
