package com.github.throyer.common.springboot.domain.user.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.throyer.common.springboot.domain.management.model.Entity;
import com.github.throyer.common.springboot.domain.role.entity.Role;
import com.github.throyer.common.springboot.domain.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "User", requiredProperties = {"id", "name", "email", "roles"})
public class UserDetails implements Entity {
    private final Long id;
    private final String name;
    private final String email;

    @JsonInclude(NON_NULL)
    private Boolean active;

    private final List<String> roles;

    public UserDetails(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.active = user.isActive();
        this.email = user.getEmail();

        this.roles = user.getRoles()
            .stream()
                .map(Role::getAuthority)
                    .toList();
    }

    public UserDetails(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = new ArrayList<>();
    }    

    public UserDetails(Long id, String name, String email, String roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        
        this.roles = ofNullable(roles)
            .map(string -> List.of(string.split(",")))
                .orElse(List.of());
    }
}
