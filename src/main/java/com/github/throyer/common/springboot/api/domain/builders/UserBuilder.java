package com.github.throyer.common.springboot.api.domain.builders;

import java.util.ArrayList;
import java.util.List;

import com.github.throyer.common.springboot.api.domain.models.entity.Role;
import com.github.throyer.common.springboot.api.domain.models.entity.User;

public class UserBuilder {
    
    private User user;
    private List<Role> roles = new ArrayList<>();

    public static UserBuilder createUser(String name) {
        return new UserBuilder(name);
    }

    public UserBuilder(String name) {
        this.user = new User();
        this.user.setName(name);
    }

    public UserBuilder(String name, List<Role> roles) {
        this.user = new User();
        this.roles = roles;
        this.user.setName(name);
    }

    public UserBuilder setEmail(String email) {
        user.setEmail(email);
        return this;
    }

    public UserBuilder setId(Long id) {
        user.setId(id);
        return this;
    }

    public UserBuilder setAtivo(Boolean active) {
        user.setActive(active);
        return this;
    }

    public UserBuilder setPassword(String password) {
        user.setPassword(password);
        return this;
    }

    public UserBuilder addRole(Role role) {
        roles.add(role);
        return this;
    }

    public UserBuilder addRole(Long id) {
        roles.add(new Role(id));
        return this;
    }

    public User build() {
        user.setRoles(roles);
        return user;
    }
}