package com.github.throyer.common.springboot.api.models.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.throyer.common.springboot.api.models.shared.BasicEntity;

import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Where(clause = BasicEntity.NON_DELETED_CLAUSE)
public class Permissao extends BasicEntity implements GrantedAuthority {

    private static final long serialVersionUID = -8524505911742593369L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Por favor, forneça um nome.")
    @Column(nullable = true, unique = true)
    private String nome;

    @JsonIgnore
    @Column(name = "deleted_nome")
    private String deletedNome;

    public Permissao() { }

    public Permissao(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDeletedNome() {
        return deletedNome;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Permissao)) {
            return false;
        }
        Permissao permissao = (Permissao) o;
        return Objects.equals(id, permissao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return this.getNome();
    }

    @JsonIgnore
    @Override
    public String getAuthority() {
        return this.getNome();
    }
    
}