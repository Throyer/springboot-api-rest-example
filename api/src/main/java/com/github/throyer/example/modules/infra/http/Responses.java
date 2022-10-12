package com.github.throyer.example.modules.infra.http;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import com.github.throyer.example.modules.shared.errors.ApiError;
import com.github.throyer.example.modules.shared.utils.JSON;
import com.github.throyer.example.modules.ssr.toasts.Toasts;

import lombok.extern.log4j.Log4j2;

/**
 * HTTP Responses.
 * <p>
 * Classe util para simplificar a geração
 * de responses para status codes comuns
 * utilizando <code>ResponseEntity</code>.
 */
@Log4j2
public class Responses {

  private Responses() {
  }

  public static final <T> ResponseEntity<T> forbidden(T body) {
    return ResponseEntity.status(403).body(body);
  }

  public static final <T> ResponseEntity<T> forbidden() {
    return ResponseEntity.status(403).build();
  }

  public static final <T> ResponseEntity<T> unauthorized(T body) {
    return ResponseEntity.status(401).body(body);
  }

  public static final <T> ResponseEntity<T> unauthorized() {
    return ResponseEntity.status(401).build();
  }

  public static final <T> ResponseEntity<T> ok(T body) {
    return ResponseEntity.ok(body);
  }

  public static final <T> ResponseEntity<T> ok() {
    return ResponseEntity.ok()
        .build();
  }

  public static final <T> ResponseEntity<T> notFound() {
    return ResponseEntity.notFound()
        .build();
  }

  public static final <T> ResponseEntity<T> badRequest(T body) {
    return ResponseEntity.badRequest()
        .body(body);
  }

  public static final <T> ResponseEntity<T> badRequest() {
    return ResponseEntity.badRequest()
        .build();
  }

  public static final <T> ResponseEntity<T> noContent() {
    return ResponseEntity.noContent().build();
  }

  public static final <T> ResponseEntity<T> noContent(T entity, CrudRepository<T, ?> repository) {
    repository.delete(entity);
    return ResponseEntity
        .noContent()
        .build();
  }

  public static final <T> ResponseEntity<T> created(T body, String location, String id) {
    return ResponseEntity.created(URI.create(String.format("/%s/%s", location, id)))
        .body(body);
  }

  public static final <T> ResponseEntity<T> created(T body) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(body);
  }

  public static final ResponseStatusException forbidden(String reason) {
    return new ResponseStatusException(HttpStatus.FORBIDDEN, reason);
  }

  public static final ResponseStatusException unauthorized(String reason) {
    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason);
  }

  public static final ResponseStatusException notFound(String reason) {
    return new ResponseStatusException(HttpStatus.NOT_FOUND, reason);
  }

  public static final ResponseStatusException InternalServerError(String reason) {
    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, reason);
  }

  public static final ResponseEntity<ApiError> fromException(ResponseStatusException exception) {
    return ResponseEntity
        .status(exception.getStatus())
        .body(new ApiError(exception.getReason(), exception.getStatus()));
  }

  public static void forbidden(HttpServletResponse response) {
    if (response.getStatus() != 200) {
      return;
    }

    try {
      response.setStatus(FORBIDDEN.value());
      response.setContentType("application/json");
      response.getWriter().write(JSON.stringify(
          new ApiError("Can't find bearer token on Authorization header.", FORBIDDEN)));
    } catch (Exception exception) {
      log.error("error writing forbidden response");
    }
  }

  public static void expired(HttpServletResponse response) {
    if (response.getStatus() != 200) {
      return;
    }

    try {
      response.setStatus(FORBIDDEN.value());
      response.setContentType("application/json");
      response.getWriter().write(JSON.stringify(
          new ApiError("Token expired or invalid.", FORBIDDEN)));
    } catch (IOException exception) {
      log.error("error writing token expired/invalid response");
    }
  }

  public static final <P> Boolean validateAndUpdateModel(Model model, P props, String propertyName,
      BindingResult result) {
    if (result.hasErrors()) {
      model.addAttribute(propertyName, props);
      Toasts.add(model, result);
      return true;
    }
    return false;
  }
}