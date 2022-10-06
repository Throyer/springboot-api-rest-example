package com.github.throyer.example.modules.roles.controllers;

import static com.github.throyer.example.modules.infra.http.Responses.ok;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.throyer.example.modules.roles.dtos.RoleInformation;
import com.github.throyer.example.modules.roles.repositories.RoleRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Roles")
@RequestMapping("/api/v1/roles")
@SecurityRequirement(name = "token")
@PreAuthorize("hasAnyAuthority('ADM')")
public class RolesController {

  @Autowired
  private RoleRepository repository;

  @GetMapping
  @Operation(summary = "Returns a list of roles")
  public ResponseEntity<List<RoleInformation>> index() {
    return ok(repository.findAll().stream().map(RoleInformation::new).toList());
  }
}