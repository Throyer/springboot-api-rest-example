package com.github.throyer.common.springboot.api.models.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
public class Usuario implements Serializable {

    public static final Integer FORCA_DA_SENHA = 10;
    public static final String  SENHA_PADRAO = "mudar123";
    public static final String  SENHA_FORTE = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static final String  MENSAGEM_SENHA_FORTE = "No mínimo 8 caracteres, com no mínimo um número, um caractere especial, uma letra maiúscula e uma letra minúscula.";

    private static final long serialVersionUID = -8080540494839892473L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O nome não pode ser NULL.")
    @NotEmpty(message = "Por favor, forneça um nome.")
    @Column(name = "nome", nullable = false)
    private String nome;
    
    @NotNull(message = "O e-mail não pode ser NULL.")
    @Email(message = "Por favor, forneça um e-mail valido.")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @JsonInclude(Include.NON_NULL)
    @NotEmpty(message = "Por favor, forneça uma senha.")
    @NotNull(message = "A senha não pode ser NULL.")
    @Pattern(regexp = SENHA_FORTE, message = MENSAGEM_SENHA_FORTE)
    @Column(name = "senha", nullable = false)
    private String senha = SENHA_PADRAO;

    @Column(name = "active", nullable = false)
    private Boolean ativo = true;

    @JsonInclude(Include.NON_NULL)
    @Column(name = "created_at")
    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime createdAt;

    @JsonInclude(Include.NON_NULL)
    @Column(name = "updated_at")
    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime updatedAt;
    
    @NotNull
    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "usuario_permissao",
            joinColumns = {
                @JoinColumn(name = "usuario_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "permissao_id")})
    private List<Permissao> permissoes;

    public Usuario() { }

    public Usuario(String nome, String email, String senha, List<Permissao> permissoes) {
        setNome(nome);
        setEmail(email);
        setSenha(senha);
        setPermissoes(permissoes);
    }

    public List<Permissao> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<Permissao> permissoes) {
        this.permissoes = permissoes;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean isAtivo() {
        return this.ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        return Objects.equals(this.id, other.id);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean validarSenha(String senha) {
        var encoder = new BCryptPasswordEncoder(FORCA_DA_SENHA);
        return encoder.matches(senha, this.senha);
    }

    @PrePersist
    private void created() {
        this.senha = new BCryptPasswordEncoder(FORCA_DA_SENHA).encode(senha);
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void updated() {
        this.updatedAt = LocalDateTime.now();
    } 

    @Override
    public String toString() {
        return this.getNome();
    }
}