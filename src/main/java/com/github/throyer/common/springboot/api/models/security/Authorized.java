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

    public Boolean isAdmin() {
        return getAuthorities()
            .stream()
                .anyMatch((role) -> role.getAuthority().equals("ADM"));
    }

    public Boolean cantModify(Long id) {
        var admin = isAdmin();
        var equals = getId().equals(id);
        if (admin) {
            return true;
        }
        return equals;
    }

    public Boolean cantRead(Long id) {
        var admin = isAdmin();
        var equals = getId().equals(id);
        if (admin) {
            return true;
        }
        return equals;
    }
 
    @Override
    public String toString() {
        return getId().toString();
    }
}