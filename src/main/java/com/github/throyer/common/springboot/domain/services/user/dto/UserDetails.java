package com.github.throyer.common.springboot.domain.services.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

import com.github.throyer.common.springboot.domain.models.entity.User;
import com.github.throyer.common.springboot.domain.models.shared.Entity;
import static java.util.Optional.ofNullable;

public class UserDetails implements Entity {
    private final Long id;
    private final String name;
    private final String email;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<String> roles;

    public UserDetails(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();

        this.roles = user.getRoles()
            .stream()
                .map(role -> role.getAuthority())
                    .toList();
    }

    public UserDetails(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = null;
    }    

    public UserDetails(Long id, String name, String email, String roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        
        this.roles = ofNullable(roles)
            .map(string -> List.of(string.split(",")))
                .orElse(List.of());
    }    
    
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }
}
