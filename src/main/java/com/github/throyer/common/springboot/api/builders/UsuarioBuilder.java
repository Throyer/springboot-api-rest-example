package com.github.throyer.common.springboot.api.builders;

import java.util.ArrayList;
import java.util.List;

import com.github.throyer.common.springboot.api.models.entity.Permissao;
import com.github.throyer.common.springboot.api.models.entity.Usuario;

public class UsuarioBuilder {
    
    private Usuario usuario;
    private List<Permissao> permissoes = new ArrayList<>(); 

    public UsuarioBuilder(String nome) {
        usuario = new Usuario();
        usuario.setNome(nome);
    }

    public UsuarioBuilder setEmail(String email) {
        usuario.setEmail(email);
        return this;
    }

    public UsuarioBuilder setId(Long id) {
        usuario.setId(id);
        return this;
    }

    public UsuarioBuilder setAtivo(Boolean ativo) {
        usuario.setAtivo(ativo);
        return this;
    }

    public UsuarioBuilder setSenha(String senha) {
        usuario.setSenha(senha);
        return this;
    }

    public UsuarioBuilder addPermissao(Permissao permissao) {
        permissoes.add(permissao);
        return this;
    }

    public Usuario build() {
        usuario.setPermissoes(permissoes);
        return usuario;
    }
}