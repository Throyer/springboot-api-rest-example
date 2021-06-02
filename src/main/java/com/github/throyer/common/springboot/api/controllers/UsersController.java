package com.github.throyer.common.springboot.api.controllers;

import com.github.throyer.common.springboot.api.models.entity.User;
import com.github.throyer.common.springboot.api.models.shared.Page;
import com.github.throyer.common.springboot.api.models.shared.Pagination;
import com.github.throyer.common.springboot.api.services.user.CreateUserService;
import com.github.throyer.common.springboot.api.services.user.FindUserService;
import com.github.throyer.common.springboot.api.services.user.RemoveUserService;
import com.github.throyer.common.springboot.api.services.user.UpdateUserService;
import com.github.throyer.common.springboot.api.services.user.dto.CreateUser;
import com.github.throyer.common.springboot.api.services.user.dto.UpdateUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/users")
@Api(tags = "/users", description = "users")
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
    @PreAuthorize("hasAnyAuthority('ADM')")
    public ResponseEntity<Page<User>> index(Pagination pagination, Sort sort) {
        return findService.find(pagination, sort);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADM', 'USER')")
    public ResponseEntity<User> show(@PathVariable Long id) {
        return findService.find(id);
    }
    
    @PostMapping
    public ResponseEntity<User> save(@Validated @RequestBody CreateUser body) {
        return createService.create(body);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADM', 'USER')")
    public ResponseEntity<User> update(
        @PathVariable Long id,
        @RequestBody @Validated UpdateUser body
    ) {
        return updateService.update(id, body);
    }
    
    @PreAuthorize("hasAnyAuthority('ADM')")
    @DeleteMapping("/{id}")
    public ResponseEntity<User> destroy(@PathVariable Long id) {
        return removeService.remove(id);
    }
}