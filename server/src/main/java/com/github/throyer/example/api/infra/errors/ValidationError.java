package com.github.throyer.example.api.infra.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
public class ValidationError {

  @JsonInclude(NON_NULL)
  @Schema(example = "genericFieldName", requiredMode = NOT_REQUIRED)
  private final String field;

  @Schema(example = "generic error message", requiredMode = REQUIRED)
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