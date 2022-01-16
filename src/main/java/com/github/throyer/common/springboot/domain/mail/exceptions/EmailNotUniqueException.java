package com.github.throyer.common.springboot.domain.mail.exceptions;

import com.github.throyer.common.springboot.domain.shared.SimpleError;
import java.util.List;

public class EmailNotUniqueException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private List<SimpleError> errors;

    public EmailNotUniqueException(List<SimpleError> errors) {
        super();
        this.errors = errors;
    }

    public EmailNotUniqueException(String message, List<SimpleError> errors) {
        super(message);
        this.errors = errors;
    }

    public EmailNotUniqueException(String message, Throwable cause, List<SimpleError> errors) {
        super(message, cause);
        this.errors = errors;
    }

    public EmailNotUniqueException(Throwable cause, List<SimpleError> errors) {
        super(cause);
        this.errors = errors;
    }

    public List<SimpleError> getErrors() {
        return errors;
    }

}