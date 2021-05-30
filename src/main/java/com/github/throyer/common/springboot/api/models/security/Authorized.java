package com.github.throyer.common.springboot.api.models.security;

import java.util.List;

import com.github.throyer.common.springboot.api.models.entity.Role;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;

public class Authorized extends User {

    private static final long serialVersionUID = 1L;

    private Long id;

    public Authorized(String username, Long id, List<Role> authorities) {
        super(username, "SECRET", authorities);
        this.id = id;
    }

    public Authorized(com.github.throyer.common.springboot.api.models.entity.User user) {
        super(
            user.getEmail(),
            user.getPassword(),
            user.isActive(),
            true,
            true,
            true,
            user.getRoles()
        );
        this.id = user.getId();
    }

    public Long getId() {
        return id;
    }

    public UsernamePasswordAuthenticationToken getAuthentication() {
        return new UsernamePasswordAuthenticationToken(this, null, getAuthorities());
    }
 
    @Override
    public String toString() {
        return getId().toString();
    }
}