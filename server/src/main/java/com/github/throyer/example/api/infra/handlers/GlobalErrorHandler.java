package com.github.throyer.example.api.infra.handlers;

import com.github.throyer.example.api.infra.errors.ApiError;
import com.github.throyer.example.api.infra.errors.ValidationError;
import com.github.throyer.example.api.infra.handlers.swagger.RequestWithoutAuthorizationResponse;
import com.github.throyer.example.api.infra.handlers.swagger.TokenExpiredInvalidResponse;
import com.github.throyer.example.api.infra.handlers.swagger.UnauthorizedResponse;
import com.github.throyer.example.api.shared.exceptions.BadRequestException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import static com.github.throyer.example.api.shared.rest.Responses.fromException;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
@ApiResponse(
  responseCode = "401",
  description = "When not authorized to perform some operation on a resource",
  content = {@Content(schema = @Schema(implementation = UnauthorizedResponse.class))}
)
@ApiResponse(
  responseCode = "403",
  description = """
      Some access problem in the request body, headers or token.
      See the message in the response for more details.
    """, content = {
  @Content(schema = @Schema(oneOf = {
    RequestWithoutAuthorizationResponse.class,
    TokenExpiredInvalidResponse.class
  }))
})
public class GlobalErrorHandler {
  @ResponseStatus(code = BAD_REQUEST)
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ApiError badRequest(MissingServletRequestParameterException exception) {
    return new ApiError(BAD_REQUEST, new ValidationError(exception.getParameterName(), exception.getMessage()));
  }

  @ResponseStatus(code = BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ApiError badRequest(MethodArgumentNotValidException exception) {
    return new ApiError(BAD_REQUEST, ValidationError.of(exception));
  }

  @ResponseStatus(code = BAD_REQUEST)
  @ExceptionHandler(BadRequestException.class)
  public ApiError badRequest(BadRequestException exception) {
    return new ApiError(BAD_REQUEST, exception.getErrors());
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiError> status(ResponseStatusException exception) {
    return fromException(exception);
  }

  @ResponseStatus(code = UNAUTHORIZED)
  @ExceptionHandler(AccessDeniedException.class)
  public ApiError unauthorized(AccessDeniedException exception) {
    return new ApiError("Not authorized.", UNAUTHORIZED);
  }
}