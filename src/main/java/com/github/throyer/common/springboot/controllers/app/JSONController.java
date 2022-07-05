package com.github.throyer.common.springboot.controllers.app;

import static com.github.throyer.common.springboot.utils.Responses.ok;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.throyer.common.springboot.domain.role.entity.Role;
import com.github.throyer.common.springboot.domain.role.repository.RoleRepository;
import com.github.throyer.common.springboot.domain.user.model.UserDetails;
import com.github.throyer.common.springboot.domain.user.repository.UserRepository;

@RestController
@RequestMapping("/app/json")
@PreAuthorize("hasAnyAuthority('ADM')")
public class JSONController {

    @Autowired
    private RoleRepository roles;

    @Autowired
    private UserRepository users;

    @GetMapping("roles")
    public ResponseEntity<List<Role>> roles() {
        return ok(roles.findAll());
    }

    @GetMapping("user/{id}")
    public ResponseEntity<UserDetails> user(@PathVariable Long id) {
        var user = users.findByIdFetchRoles(id)
            .orElseThrow();

        return ok(new UserDetails(user));
    }
}