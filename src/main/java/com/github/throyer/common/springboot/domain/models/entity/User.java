package com.github.throyer.common.springboot.domain.models.entity;

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
import com.github.throyer.common.springboot.domain.models.pagination.SortableProperty;
import com.github.throyer.common.springboot.domain.models.security.Authorized;
import com.github.throyer.common.springboot.domain.models.shared.HasEmail;
import com.github.throyer.common.springboot.domain.services.user.dto.UpdateUser;

import org.hibernate.annotations.Where;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "user")
@Where(clause = Auditable.NON_DELETED_CLAUSE)
public class User extends Auditable implements Serializable, HasEmail {

    public static final Integer PASSWORD_STRENGTH = 10;
    public static final String DELETE_SQL = """
        UPDATE
            #{#entityName}
        SET
            deleted_email = (
                SELECT
                    email
                FROM
                    #{#entityName}
                WHERE id = ?1),
            email = NULL,
            deleted_at = CURRENT_TIMESTAMP,
            active = 0,
            deleted_by = ?#{principal?.id}
        WHERE id = ?1
    """;

    private static final long serialVersionUID = -8080540494839892473L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SortableProperty(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @SortableProperty(name = "email")
    @Column(name = "email", unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "deleted_email")
    private String deletedEmail;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

    @SortableProperty(name = "role", column = "roles_name")
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
        joinColumns = {
            @JoinColumn(name = "user_id")},
        inverseJoinColumns = {
            @JoinColumn(name = "role_id")})
    private List<Role> roles;

    public User() { }

    public User(Long id) {
        setId(id);
    }

    public User(String name, String email, String password, List<Role> roles) {
        setName(name);
        setEmail(email);
        setPassword(password);
        setRoles(roles);
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nome) {
        this.name = nome;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeletedEmail() {
        return deletedEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean contains(String search) {
        if (Objects.nonNull(getRoles())) {
            return roles.stream()
            .anyMatch(role -> role.compare(search));
        }
        return false;
    }

    public void merge(UpdateUser dto) {
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
        var encoder = new BCryptPasswordEncoder(PASSWORD_STRENGTH);
        return encoder.matches(password, this.password);
    }

    @PrePersist
    private void created() {
        this.password = new BCryptPasswordEncoder(PASSWORD_STRENGTH).encode(password);
    }

    @Override
    public String toString() {
        return Objects.nonNull(getName()) ? name : "null";
    }
}