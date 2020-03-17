package com.github.throyer.common.springboot.api.models.security;

import java.util.List;

import com.github.throyer.common.springboot.api.models.entity.Permissao;
import com.github.throyer.common.springboot.api.models.entity.Usuario;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;

public class Authorized extends User {

    private static final long serialVersionUID = 1L;

    private Long id;

    public Authorized(String username, Long id, List<Permissao> authorities) {
        super(username, "SECRET", authorities);
        this.id = id;
    }

    public Authorized(Usuario usuario) {
        super(
            usuario.getEmail(),
            usuario.getSenha(),
            usuario.isAtivo(),
            true,
            true,
            true,
            usuario.getPermissoes()
        );
        this.id = usuario.getId();
    }

    public Long getId() {
        return id;
    }

    public UsernamePasswordAuthenticationToken getAuthentication() {
        return new UsernamePasswordAuthenticationToken(this, null, getAuthorities());
    }
 
}