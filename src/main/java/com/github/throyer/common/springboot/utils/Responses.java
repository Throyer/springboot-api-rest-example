package com.github.throyer.common.springboot.utils;

import java.net.URI;

import com.github.throyer.common.springboot.domain.management.model.Entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

/**
 * HTTP Responses.
 * 
 * Classe util para simplificar a geração
 * de responses para status codes comuns
 * utilizando <code>ResponseEntity</code>.
 */
public class Responses {

    private Responses() { }

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

    public static final <T extends Entity> ResponseEntity<T> created(T entity, String location) {
        return ResponseEntity.created(URI.create(String.format("/%s/%s", location, entity.getId())))
            .body(entity);
    }

    public static final <T> ResponseEntity<T> created(T body, String location, Long id) {
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
    
    public static final <P> Boolean validate(Model model, P props, String propertyName, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute(propertyName, props);
            Toasts.add(model, result);            
            return true;
        }
        return false;
    }
}