package com.github.throyer.common.springboot.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class Error {

    @JsonInclude(NON_NULL)
    private String field;

    @JsonInclude(NON_NULL)
    private String message;

    @JsonInclude(NON_NULL)
    private Integer status;

    public Error(FieldError error) {
        this.field = error.getField();
        this.message = error.getDefaultMessage();
    }

    public Error(ObjectError error) {
        this.message = error.getDefaultMessage();
    }

    public Error(String filed, String message) {
        this.field = filed;
        this.message = message;
    }

    public Error(String message, Integer status) {
        this.message = message;
        this.status = status;
    }

    public Error(String message, HttpStatus status) {
        this.message = message;
        this.status = status.value();
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static final List<Error> of(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map((error) -> (new Error((FieldError) error)))
                .toList();
    }
}