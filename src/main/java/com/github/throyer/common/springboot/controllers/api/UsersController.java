package com.github.throyer.common.springboot.controllers.api;

import com.github.throyer.common.springboot.domain.pagination.model.Page;
import com.github.throyer.common.springboot.domain.user.form.CreateUserProps;
import com.github.throyer.common.springboot.domain.user.form.UpdateUserProps;
import com.github.throyer.common.springboot.domain.user.model.UserDetails;
import com.github.throyer.common.springboot.domain.user.service.CreateUserService;
import com.github.throyer.common.springboot.domain.user.service.FindUserService;
import com.github.throyer.common.springboot.domain.user.service.RemoveUserService;
import com.github.throyer.common.springboot.domain.user.service.UpdateUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.github.throyer.common.springboot.utils.Responses.created;
import static com.github.throyer.common.springboot.utils.Responses.ok;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@Tag(name = "Users")
@RequestMapping("/api/users")
public class UsersController {
        
    private final CreateUserService createService;
    private final UpdateUserService updateService;
    private final RemoveUserService removeService;
    private final FindUserService findService;

    @Autowired
    public UsersController(
        CreateUserService createService,
        UpdateUserService updateService,
        RemoveUserService removeService,
        FindUserService findService
    ) {
        this.createService = createService;
        this.updateService = updateService;
        this.removeService = removeService;
        this.findService = findService;
    }

    @GetMapping
    @SecurityRequirement(name = "token")
    @PreAuthorize("hasAnyAuthority('ADM')")
    @Operation(summary = "Returns a list of users")
    public ResponseEntity<Page<UserDetails>> index(
        Optional<Integer> page,
        Optional<Integer> size
    ) {
        var content = findService.find(page, size);
        return ok(content.map(UserDetails::new));
    }
    
    @GetMapping("/{id}")
    @SecurityRequirement(name = "token")
    @PreAuthorize("hasAnyAuthority('ADM', 'USER')")
    @Operation(summary = "Show user info")
    public ResponseEntity<UserDetails> show(@PathVariable Long id) {
        var user = findService.find(id);
        return ok(new UserDetails(user));
    }
    
    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Register a new user", description = "Returns the new user")
    public ResponseEntity<UserDetails> save(
        @Validated @RequestBody CreateUserProps props
    ) {
        var user = createService.create(props);
        return created(new UserDetails(user), "api/users");
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
        return ok(new UserDetails(user));
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @SecurityRequirement(name = "token")
    @Operation(summary = "Delete user")
    public void destroy(@PathVariable Long id) {
        removeService.remove(id);
    }
}