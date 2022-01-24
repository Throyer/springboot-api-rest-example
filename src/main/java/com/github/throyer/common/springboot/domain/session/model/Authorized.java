package com.github.throyer.common.springboot.domain.session.model;

import com.github.throyer.common.springboot.domain.role.entity.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.List;

public class Authorized extends User {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String name;

    public Authorized(Long id, List<Role> authorities) {
        super("USERNAME", "SECRET", authorities);
        this.id = id;
        this.name = "";
    }

    public Authorized(com.github.throyer.common.springboot.domain.user.entity.User user) {
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
        this.name = user.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public UsernamePasswordAuthenticationToken getAuthentication() {
        return new UsernamePasswordAuthenticationToken(this, null, getAuthorities());
    }

    public Boolean isAdmin() {
        return getAuthorities()
            .stream()
                .anyMatch((role) -> role.getAuthority().equals("ADM"));
    }

    public Boolean itsMeOrSessionIsADM(Long id) {
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