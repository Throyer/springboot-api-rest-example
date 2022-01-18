package com.github.throyer.common.springboot.errors;

import static com.github.throyer.common.springboot.utils.JsonUtils.toJson;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.github.throyer.common.springboot.domain.mail.exceptions.EmailNotUniqueException;

import com.github.throyer.common.springboot.utils.Responses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class ValidationHandlers {

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<Error> badRequest(MethodArgumentNotValidException exception) {
        return Error.of(exception);
    }    

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(EmailNotUniqueException.class)
    public List<Error> badRequest(EmailNotUniqueException exception) {
        return exception.getErrors();
    }    

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Error> status(ResponseStatusException exception) {
        return Responses.fromException(exception);
    }    

    @ResponseStatus(code = UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public Error unauthorized(AccessDeniedException exception) {
        return new Error(exception.getMessage(), UNAUTHORIZED);
    }
}