package com.github.throyer.example.modules.shared.errors;

import java.util.List;

import org.springframework.web.bind.MethodArgumentNotValidException;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ValidationError {

  @Schema(example = "genericFieldName")
  private final String field;

  @Schema(example = "generic error message", required = true)
  private final String message;

  public ValidationError(String message) {
    this.field = null;
    this.message = message;
  }

  public ValidationError(org.springframework.validation.FieldError error) {
    this.field = error.getField();
    this.message = error.getDefaultMessage();
  }

  public ValidationError(String field, String message) {
    this.field = field;
    this.message = message;
  }

  public static List<ValidationError> of(MethodArgumentNotValidException exception) {
    return exception.getBindingResult()
      .getAllErrors()
        .stream()
          .map((error) -> (new ValidationError((org.springframework.validation.FieldError) error)))
        .toList();
  }
}