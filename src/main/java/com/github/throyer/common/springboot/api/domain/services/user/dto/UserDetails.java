package com.github.throyer.common.springboot.api.domain.services.user.dto;

import java.util.List;

import com.github.throyer.common.springboot.api.domain.models.entity.User;
import com.github.throyer.common.springboot.api.domain.models.shared.Entity;

public class UserDetails implements Entity {
    private final Long id;
    private final String name;
    private final String email;
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
