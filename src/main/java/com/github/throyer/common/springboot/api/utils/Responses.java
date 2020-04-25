package com.github.throyer.common.springboot.api.utils;

import java.net.URI;

import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

/**
 * HTTP Responses.
 * 
 * Classe util para simplificar a geração
 * de reponses para status codes comuns
 * utilizando <code>ResponseEntity</code>.
 */
public class Responses {

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

    public static final <T> ResponseEntity<T> BadRequest(T body) {
        return ResponseEntity.badRequest()
            .body(body);
    }
    
    public static final <T> ResponseEntity<T> BadRequest() {
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

    public static final <T> ResponseEntity<T> created(T body, String location, Object id) {
        return ResponseEntity.created(URI.create(String.format("/%s/%s", location, id)))
            .body(body);
    }

    public static final <T> ResponseEntity<T> created(T body) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(body);
    }

    public static final ResponseStatusException notFound(String reason) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, reason);
    }

    public static final ResponseStatusException InternalServerError(String reason) {
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, reason);
    }
}