package com.github.throyer.common.springboot.api.models.validation;

import java.util.List;

public class InvalidSortException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private List<SimpleError> errors;

    public InvalidSortException(List<SimpleError> errors) {
        super();
        this.errors = errors;
    }

    public InvalidSortException(String message, List<SimpleError> errors) {
        super(message);
        this.errors = errors;
    }

    public InvalidSortException(String message, Throwable cause, List<SimpleError> errors) {
        super(message, cause);
        this.errors = errors;
    }

    public InvalidSortException(Throwable cause, List<SimpleError> errors) {
        super(cause);
        this.errors = errors;
    }

    public List<SimpleError> getErrors() {
        return errors;
    }
}
