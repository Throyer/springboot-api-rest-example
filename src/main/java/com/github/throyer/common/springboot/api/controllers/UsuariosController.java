package com.github.throyer.common.springboot.api.controllers;

import static com.github.throyer.common.springboot.api.utils.Responses.created;
import static com.github.throyer.common.springboot.api.utils.Responses.noContent;
import static com.github.throyer.common.springboot.api.utils.Responses.notFound;
import static com.github.throyer.common.springboot.api.utils.Responses.ok;

import com.github.throyer.common.springboot.api.models.entity.Usuario;
import com.github.throyer.common.springboot.api.models.shared.Pagination;
import com.github.throyer.common.springboot.api.repositories.UsuarioRepository;

import org.springframework.beans.BeanUtils;
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
@RequestMapping("/usuarios")
@PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
@Api(tags = "/usuarios", description = "usuarios")
public class UsuariosController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping
    public ResponseEntity<Page<Usuario>> index(Pagination pagination) {
        return ok(repository.findAll(pagination.build()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> show(@PathVariable Long id) {
        return repository.findById(id)
            .map(usuario -> ok(usuario))
                .orElseGet(() -> notFound());
    }

    @PostMapping
    public ResponseEntity<Usuario> save(@Validated @RequestBody Usuario usuario) {
        var novo = repository.save(usuario);
        return created(novo, "usuarios", novo.getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(
        @PathVariable Long id,
        @RequestBody @Validated Usuario usuario
    ) {

        var persist = repository.findById(id)
            .orElseThrow(() -> notFound("Usuario não encontrado"));

        BeanUtils.copyProperties(usuario, persist, "id");

        return ok(repository.save(persist));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> destroy(@PathVariable Long id) {
        return repository.findById(id)
            .map((usuario) -> noContent(usuario, repository))
                .orElseGet(() -> notFound());
    }
}