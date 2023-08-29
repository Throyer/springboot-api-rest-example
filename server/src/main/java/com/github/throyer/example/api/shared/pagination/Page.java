package com.github.throyer.example.api.shared.pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter
public class Page<T> {
  private final Collection<? extends T> content;

  @Schema(example = "3")
  private final Integer page;

  @Schema(example = "10")
  private final Integer size;

  @Schema(example = "8")
  private final Integer totalPages;

  @Schema(example = "72")
  private final Long totalElements;

  public Page(org.springframework.data.domain.Page<T> page) {
    this.content = page.getContent();
    this.page = page.getNumber();
    this.size = page.getSize();
    this.totalPages = page.getTotalPages();
    this.totalElements = page.getTotalElements();
  }

  public Page(Collection<? extends T> content, Integer page, Integer size, Long count) {
    this.content = content;
    this.page = page;
    this.size = size;
    this.totalPages = (int) Math.ceil((double) count / size);
    this.totalElements = count;
  }

  public static <T> Page<T> of(org.springframework.data.domain.Page<T> page) {
    return new Page<>(page);
  }

  public static <T> Page<T> of(Collection<? extends T> content, Integer page, Integer size, Long count) {
    return new Page<T>(content, page, size, count);
  }

  public <U> Page<U> map(Function<? super T, ? extends U> converter) {
    var content = this.content.stream().map(converter).toList();
    return new Page<U>(content, this.page, this.size, this.totalElements);
  }

  public static <T> Page<T> empty() {
    return new Page<>(List.of(), 0, 0, 0L);
  }
}
