package com.github.throyer.common.springboot.api.models.validation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class SimpleError {

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("campo")
    private String field;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("messagem")
    private String message;

    public SimpleError(FieldError error) {
        this.field = error.getField();
        this.message = error.getDefaultMessage();
    }

    public SimpleError(ObjectError error) {
        this.message = error.getDefaultMessage();
    }

    public SimpleError(String filed, String message) {
        this.field = filed;
        this.message = message;
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
}