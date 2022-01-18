package com.github.throyer.common.springboot.controllers.api;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import static com.github.throyer.common.springboot.utils.Responses.created;
import static com.github.throyer.common.springboot.utils.Responses.ok;

import com.github.throyer.common.springboot.domain.pagination.model.Page;
import com.github.throyer.common.springboot.domain.user.service.FindUserService;
import com.github.throyer.common.springboot.domain.user.service.RemoveUserService;
import com.github.throyer.common.springboot.domain.user.model.CreateUserProps;
import com.github.throyer.common.springboot.domain.user.service.CreateUserService;
import com.github.throyer.common.springboot.domain.user.model.UpdateUserProps;
import com.github.throyer.common.springboot.domain.user.service.FindUserByIdService;
import com.github.throyer.common.springboot.domain.user.service.UpdateUserService;
import com.github.throyer.common.springboot.domain.user.model.UserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.Optional;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Users")
@RequestMapping("/api/users")
public class UsersController {
        
    private final CreateUserService createService;
    private final UpdateUserService updateService;
    private final RemoveUserService removeService;
    private final FindUserService findService;
    private final FindUserByIdService findByIdService;

    @Autowired
    public UsersController(
        CreateUserService createService,
        UpdateUserService updateService,
        RemoveUserService removeService,
        FindUserService findService,
        FindUserByIdService findByIdService
    ) {
        this.createService = createService;
        this.updateService = updateService;
        this.removeService = removeService;
        this.findService = findService;
        this.findByIdService = findByIdService;
    }

    @GetMapping
    @SecurityRequirement(name = "token")
    @PreAuthorize("hasAnyAuthority('ADM')")
    @Operation(summary = "Returns a list of users")
    public ResponseEntity<Page<UserDetails>> index(
        Optional<Integer> page,
        Optional<Integer> size
    ) {
        var content = findService.findAll(page, size);
        return ok(content);
    }
    
    @GetMapping("/{id}")
    @SecurityRequirement(name = "token")
    @PreAuthorize("hasAnyAuthority('ADM', 'USER')")
    @Operation(summary = "Show user info")
    public ResponseEntity<UserDetails> show(@PathVariable Long id) {
        var user = findByIdService.find(id);
        return ok(user);
    }
    
    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Register a new user", description = "Returns the new user")
    public ResponseEntity<UserDetails> save(
        @Validated @RequestBody CreateUserProps props
    ) {
        props.validate();
        var user = createService.create(props);
        return created(user, "api/users");
    }
    
    @PutMapping("/{id}")
    @SecurityRequirement(name = "token")
    @PreAuthorize("hasAnyAuthority('ADM', 'USER')")
    @Operation(summary = "Update user data")
    public ResponseEntity<UserDetails> update(
        @PathVariable Long id,
        @RequestBody @Validated UpdateUserProps body
    ) {
        var user = updateService.update(id, body);
        return ok(user);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @SecurityRequirement(name = "token")
    @Operation(summary = "Delete user")
    public void destroy(@PathVariable Long id) {
        removeService.remove(id);
    }
}