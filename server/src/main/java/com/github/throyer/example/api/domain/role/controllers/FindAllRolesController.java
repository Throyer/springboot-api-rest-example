package com.github.throyer.example.api.domain.role.controllers;

import static com.github.throyer.example.api.shared.rest.Responses.ok;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.throyer.example.api.domain.role.dtos.RoleInformation;
import com.github.throyer.example.api.domain.role.persistence.repositories.RoleRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@Tag(name = "Roles")
@RequestMapping("/roles")
@SecurityRequirement(name = "jwt")
@PreAuthorize("hasAnyAuthority('ADM')")
public class FindAllRolesController {
  private final RoleRepository repository;
  @GetMapping
  @Operation(summary = "Returns a list of roles")
  public ResponseEntity<List<RoleInformation>> index() {
    var roles = repository.findAll();    
    return ok(roles.stream().map(RoleInformation::new).toList());
  }
}
