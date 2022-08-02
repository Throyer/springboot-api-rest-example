package com.github.throyer.common.springboot.errors;

import com.github.throyer.common.springboot.errors.exceptions.BadRequestException;
import com.github.throyer.common.springboot.errors.model.ApiError;
import com.github.throyer.common.springboot.errors.model.ValidationError;
import com.github.throyer.common.springboot.utils.Responses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class ValidationHandlers {

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Collection<ValidationError> badRequest(MethodArgumentNotValidException exception) {
        return ValidationError.of(exception);
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Collection<ValidationError> badRequest(BadRequestException exception) {
        return exception.getErrors();
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> status(ResponseStatusException exception) {
        return Responses.fromException(exception);
    }    

    @ResponseStatus(code = UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public ApiError unauthorized(AccessDeniedException exception) {
        return new ApiError("Not authorized.", UNAUTHORIZED);
    }
}