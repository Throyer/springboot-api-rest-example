package com.github.throyer.common.springboot.domain.mail.exceptions;

import com.github.throyer.common.springboot.errors.Error;
import java.util.List;

public class EmailNotUniqueException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private List<Error> errors;

    public EmailNotUniqueException(List<Error> errors) {
        super();
        this.errors = errors;
    }

    public EmailNotUniqueException(String message, List<Error> errors) {
        super(message);
        this.errors = errors;
    }

    public EmailNotUniqueException(String message, Throwable cause, List<Error> errors) {
        super(message, cause);
        this.errors = errors;
    }

    public EmailNotUniqueException(Throwable cause, List<Error> errors) {
        super(cause);
        this.errors = errors;
    }

    public List<Error> getErrors() {
        return errors;
    }

}