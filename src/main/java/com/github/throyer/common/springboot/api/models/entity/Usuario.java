package com.github.throyer.common.springboot.api.models.entity;

import java.io.Serializable;
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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.throyer.common.springboot.api.models.shared.BasicEntity;

import org.hibernate.annotations.Where;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Where(clause = BasicEntity.NON_DELETED_CLAUSE)
public class Usuario extends BasicEntity implements Serializable {

    public static final Integer FORCA_DA_SENHA = 10;
    public static final String SENHA_PADRAO = "mudar123";
    public static final String SENHA_FORTE = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static final String MENSAGEM_SENHA_FORTE = "No mínimo 8 caracteres, com no mínimo um número, um caractere especial, uma letra maiúscula e uma letra minúscula.";
    public static final String DELETE_SQL = "UPDATE #{#entityName} SET deleted_email = (SELECT email FROM #{#entityName} WHERE id = ?1), email = NULL, deleted_at = CURRENT_TIMESTAMP, active = 0 WHERE id = ?1";

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
    @Column(name = "email", unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "deleted_email")
    private String deletedEmail;

    @JsonInclude(Include.NON_NULL)
    @NotEmpty(message = "Por favor, forneça uma senha.")
    @NotNull(message = "A senha não pode ser NULL.")
    @Pattern(regexp = SENHA_FORTE, message = MENSAGEM_SENHA_FORTE)
    @Column(name = "senha", nullable = false)
    private String senha = SENHA_PADRAO;
    
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

    public String getDeletedEmail() {
        return deletedEmail;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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

    public Boolean validarSenha(String senha) {
        var encoder = new BCryptPasswordEncoder(FORCA_DA_SENHA);
        return encoder.matches(senha, this.senha);
    }

    @PrePersist
    private void created() {
        this.senha = new BCryptPasswordEncoder(FORCA_DA_SENHA)
            .encode(senha);
    }

    @Override
    public String toString() {
        return this.getNome();
    }
}