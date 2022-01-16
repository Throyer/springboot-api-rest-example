package com.github.throyer.common.springboot.controllers.api;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.github.throyer.common.springboot.domain.models.entity.User;
import com.github.throyer.common.springboot.domain.models.pagination.Page;

import com.github.throyer.common.springboot.domain.services.user.CreateUserService;
import com.github.throyer.common.springboot.domain.services.user.FindUserService;
import com.github.throyer.common.springboot.domain.services.user.RemoveUserService;
import com.github.throyer.common.springboot.domain.services.user.UpdateUserService;
import com.github.throyer.common.springboot.domain.services.user.dto.CreateUserApi;
import com.github.throyer.common.springboot.domain.services.user.dto.UpdateUser;
import com.github.throyer.common.springboot.domain.services.user.dto.UserDetails;
import static com.github.throyer.common.springboot.utils.Responses.ok;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Optional;

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
@RequestMapping("/api/users")
public class UsersController {
    
    @Autowired
    private CreateUserService createService;
    
    @Autowired
    private UpdateUserService updateService;
    
    @Autowired
    private RemoveUserService removeService;
    
    @Autowired
    private FindUserService findService;
    
    @GetMapping
    @SecurityRequirement(name = "token")
    @PreAuthorize("hasAnyAuthority('ADM')")
    public ResponseEntity<Page<UserDetails>> index(
        Optional<Integer> page,
        Optional<Integer> size
    ) {
        var result = findService.findAll(page, size);
        return ok(result);
    }
    
    @GetMapping("/{id}")
    @SecurityRequirement(name = "token")
    @PreAuthorize("hasAnyAuthority('ADM', 'USER')")
    public ResponseEntity<UserDetails> show(@PathVariable Long id) {
        return findService.find(id);
    }
    
    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<UserDetails> save(@Validated @RequestBody CreateUserApi body) {
        return createService.create(body);
    }
    
    @PutMapping("/{id}")
    @SecurityRequirement(name = "token")
    @PreAuthorize("hasAnyAuthority('ADM', 'USER')")
    public ResponseEntity<UserDetails> update(
        @PathVariable Long id,
        @RequestBody @Validated UpdateUser body
    ) {
        return updateService.update(id, body);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @SecurityRequirement(name = "token")
    @PreAuthorize("hasAnyAuthority('ADM')")
    public ResponseEntity<User> destroy(@PathVariable Long id) {
        return removeService.remove(id);
    }
}