package com.github.throyer.common.springboot.api.builders;

import java.util.ArrayList;
import java.util.List;

import com.github.throyer.common.springboot.api.models.entity.Role;
import com.github.throyer.common.springboot.api.models.entity.User;

public class UserBuilder {
    
    private User user;
    private List<Role> roles = new ArrayList<>(); 

    public UserBuilder(String nome) {
        user = new User();
        user.setName(nome);
    }

    public UserBuilder setEmail(String email) {
        user.setEmail(email);
        return this;
    }

    public UserBuilder setId(Long id) {
        user.setId(id);
        return this;
    }

    public UserBuilder setAtivo(Boolean ativo) {
        user.setActive(ativo);
        return this;
    }

    public UserBuilder setPassword(String senha) {
        user.setPassword(senha);
        return this;
    }

    public UserBuilder addRole(Role role) {
        roles.add(role);
        return this;
    }

    public User build() {
        user.setRoles(roles);
        return user;
    }
}