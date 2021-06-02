package com.github.throyer.common.springboot.api.controllers;

import static com.github.throyer.common.springboot.api.utils.Responses.ok;

import java.util.List;

import com.github.throyer.common.springboot.api.domain.entity.Role;
import com.github.throyer.common.springboot.api.repositories.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/roles")
@PreAuthorize("hasAnyAuthority('ADM')")
@Api(tags = "/roles", description = "roles")
public class RolesController {

    @Autowired
    private RoleRepository repository;

    @GetMapping
    public ResponseEntity<List<Role>> index() {
        return ok(repository.findAll());
    }
}