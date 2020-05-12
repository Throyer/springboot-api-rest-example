package com.github.throyer.common.springboot.api.configurations;

import java.util.List;
import java.util.stream.Collectors;

import com.github.throyer.common.springboot.api.models.validation.EmailNotUniqueException;
import com.github.throyer.common.springboot.api.models.validation.SimpleError;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationResolver {

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
}