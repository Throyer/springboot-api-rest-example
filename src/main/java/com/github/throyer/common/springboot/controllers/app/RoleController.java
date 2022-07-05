package com.github.throyer.common.springboot.controllers.app;

import static com.github.throyer.common.springboot.utils.Responses.ok;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.throyer.common.springboot.domain.role.entity.Role;
import com.github.throyer.common.springboot.domain.role.repository.RoleRepository;

@RestController
@RequestMapping("/app/roles")
@PreAuthorize("hasAnyAuthority('ADM')")
public class RoleController {

    @Autowired
    private RoleRepository repository;

    @GetMapping
    public ResponseEntity<List<Role>> index() {
        return ok(repository.findAll());
    }
}