package com.github.throyer.common.springboot.errors.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@Getter
@Schema(requiredProperties = {"message"})
public class ValidationError {
    private final String field;
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
                        .map((error) -> (new ValidationError((FieldError) error)))
                .toList();
    }
}
