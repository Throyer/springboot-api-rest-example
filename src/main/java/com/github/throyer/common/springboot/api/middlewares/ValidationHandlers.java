package com.github.throyer.common.springboot.api.middlewares;

import java.util.List;
import java.util.stream.Collectors;

import com.github.throyer.common.springboot.api.domain.validation.EmailNotUniqueException;
import com.github.throyer.common.springboot.api.domain.validation.InvalidSortException;
import com.github.throyer.common.springboot.api.domain.validation.SimpleError;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationHandlers {

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<SimpleError> badRequest(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
            .getAllErrors()
                .stream()
                    .map((error) -> (new SimpleError((FieldError)error)))
                        .collect(Collectors.toList());
    }    

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailNotUniqueException.class)
    public List<SimpleError> badRequest(EmailNotUniqueException exception) {
        return exception.getErrors();
    }    

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidSortException.class)
    public List<SimpleError> badRequest(InvalidSortException exception) {
        return exception.getErrors();
    }    

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public SimpleError badRequest(AccessDeniedException exception) {
        return new SimpleError(exception.getMessage(), 403);
    }    
}