package com.github.throyer.common.springboot.api.controllers;

import static com.github.throyer.common.springboot.api.utils.EmailValidationUtils.validateEmailUniquenessEdition;
import static com.github.throyer.common.springboot.api.utils.EmailValidationUtils.validateEmailUniqueness;
import static com.github.throyer.common.springboot.api.utils.Responses.created;
import static com.github.throyer.common.springboot.api.utils.Responses.noContent;
import static com.github.throyer.common.springboot.api.utils.Responses.notFound;
import static com.github.throyer.common.springboot.api.utils.Responses.ok;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.security.Principal;

import com.github.throyer.common.springboot.api.models.entity.User;
import com.github.throyer.common.springboot.api.models.shared.Pagination;
import com.github.throyer.common.springboot.api.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasAnyAuthority('ADM')")
@Api(tags = "/users", description = "users")
public class UsersController {

    @Autowired
    private UserRepository repository;

    @GetMapping
    public ResponseEntity<Page<User>> index(Pagination pagination) {
        return ok(repository.findAll(pagination.build()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> show(@PathVariable Long id) {
        return repository.findById(id)
            .map(user -> ok(user))
                .orElseGet(() -> notFound());
    }

    @PostMapping
    public ResponseEntity<User> save(@Validated @RequestBody User user) {
        
        validateEmailUniqueness(user);

        var newUser = repository.save(user);

        return created(newUser, "users", newUser.getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(
        @PathVariable Long id,
        @RequestBody @Validated User novo
    ) {

        var atual = repository.findById(id)
            .orElseThrow(() -> notFound("Usuário não encontrado"));
        
        validateEmailUniquenessEdition(novo, atual);

        copyProperties(novo, atual, "id", "password", "createdAt", "updatedAt", "deletedAt");

        return ok(repository.save(atual));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> destroy(@PathVariable Long id, Principal principal) {
        return repository.findById(id)
            .map(user -> noContent(user, repository))
                .orElseGet(() -> notFound());
    }
}