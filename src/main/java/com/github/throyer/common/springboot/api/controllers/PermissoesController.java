package com.github.throyer.common.springboot.api.controllers;

import static com.github.throyer.common.springboot.api.utils.Responses.ok;

import java.util.List;

import com.github.throyer.common.springboot.api.models.entity.Permissao;
import com.github.throyer.common.springboot.api.repositories.PermissaoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/permissoes")
@PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
@Api(tags = "/permissoes", description = "permissoes")
public class PermissoesController {

    @Autowired
    private PermissaoRepository repository;

    @GetMapping
    public ResponseEntity<List<Permissao>> index() {
        return ok(repository.findAll());
    }
}