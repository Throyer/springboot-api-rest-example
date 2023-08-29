package com.github.throyer.example.api.infra.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.util.Collection;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
public class ApiError {
  @Schema(example = "generic error message", requiredMode = REQUIRED)
  private final String message;

  @Schema(example = "999", requiredMode = REQUIRED)
  private final Integer status;

  @JsonInclude(NON_NULL)
  @Schema(requiredMode = NOT_REQUIRED)
  public final Collection<ValidationError> errors;

  public ApiError(String message, Integer status) {
    this.message = message;
    this.status = status;
    this.errors = null;
  }

  public ApiError(String message, HttpStatusCode status) {
    this.message = message;
    this.status = status.value();
    this.errors = null;
  }

  public ApiError(HttpStatusCode status, Collection<ValidationError> errors) {
    this.message = "Check the 'errors' property for more details.";
    this.status = status.value();
    this.errors = errors;
  }

  public ApiError(HttpStatusCode status, String error) {
    this.message = "Check the 'errors' property for more details.";
    this.status = status.value();
    this.errors = List.of(new ValidationError(error));
  }

  public ApiError(HttpStatusCode status, ValidationError error) {
    this.message = "Check the 'errors' property for more details.";
    this.status = status.value();
    this.errors = List.of(error);
  }
}
