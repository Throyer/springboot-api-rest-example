package com.github.throyer.common.springboot.domain.user.entity;

import java.io.Serializable;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.github.throyer.common.springboot.domain.user.model.CreateUserProps;

import com.github.throyer.common.springboot.domain.session.model.Authorized;
import com.github.throyer.common.springboot.domain.user.model.UpdateUserProps;
import com.github.throyer.common.springboot.domain.management.entity.Auditable;
import com.github.throyer.common.springboot.domain.role.entity.Role;
import com.github.throyer.common.springboot.domain.management.model.Addressable;
import static com.github.throyer.common.springboot.domain.management.repository.Queries.NON_DELETED_CLAUSE;
import static com.github.throyer.common.springboot.utils.Constants.SECURITY.PASSWORD_ENCODER;

import lombok.Data;

import org.hibernate.annotations.Where;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@Entity
@Table(name = "user")
@Where(clause = NON_DELETED_CLAUSE)
public class User extends Auditable implements Serializable, Addressable {

    public static final Integer PASSWORD_STRENGTH = 10;

    private static final long serialVersionUID = -8080540494839892473L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "deleted_email")
    private String deletedEmail;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
        joinColumns = {
            @JoinColumn(name = "user_id")},
        inverseJoinColumns = {
            @JoinColumn(name = "role_id")})
    private List<Role> roles;

    public User() { }

    public User(Long id) {
        this.id = id;
    }

    public User(String name, String email, String password, List<Role> roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(CreateUserProps props, List<Role> roles) {
        this.name = props.getName();
        this.email = props.getEmail();
        this.password = props.getPassword();
        this.roles = roles;
    }
    
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void merge(UpdateUserProps dto) {
        setName(dto.getName());
        setEmail(dto.getEmail());
    }

    public void updatePassword(String newPassword) {
        this.password = new BCryptPasswordEncoder(PASSWORD_STRENGTH)
            .encode(newPassword);
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
        final User other = (User) obj;
        return Objects.equals(this.id, other.id);
    }

    public boolean isPrincipal(Principal principal) {
        if (Objects.nonNull(principal) && principal instanceof Authorized authorized) {
            return getId().equals(authorized.getId());
        }
        return false;
    }

    public boolean isPrincipal(Authorized authorized) {
        if (Objects.nonNull(authorized)) {
            return getId().equals(authorized.getId());
        }
        return false;
    }

    public Boolean validatePassword(String password) {
        return PASSWORD_ENCODER.matches(password, this.password);
    }

    @PrePersist
    private void created() {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    @Override
    public String toString() {
        return Objects.nonNull(getName()) ? name : "null";
    }
}