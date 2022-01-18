package com.github.throyer.common.springboot.controllers.api;

import static com.github.throyer.common.springboot.utils.Responses.ok;

import java.util.List;

import com.github.throyer.common.springboot.domain.role.entity.Role;
import com.github.throyer.common.springboot.domain.role.repository.RoleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Roles")
@RequestMapping("/api/roles")
@SecurityRequirement(name = "token")
@PreAuthorize("hasAnyAuthority('ADM')")
public class RolesController {

    @Autowired
    private RoleRepository repository;

    @GetMapping
    @Operation(summary = "Returns a list of roles")
    public ResponseEntity<List<Role>> index() {
        return ok(repository.findAll());
    }
}