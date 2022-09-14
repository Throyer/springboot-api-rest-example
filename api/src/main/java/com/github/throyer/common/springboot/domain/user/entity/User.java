package com.github.throyer.common.springboot.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.throyer.common.springboot.domain.mail.model.Addressable;
import com.github.throyer.common.springboot.domain.management.entity.Auditable;
import com.github.throyer.common.springboot.domain.role.entity.Role;
import com.github.throyer.common.springboot.domain.user.form.CreateUserProps;
import com.github.throyer.common.springboot.domain.user.form.UpdateUserProps;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static com.github.throyer.common.springboot.constants.SECURITY.ENCODER;
import static com.github.throyer.common.springboot.domain.management.repository.Queries.NON_DELETED_CLAUSE;
import static com.github.throyer.common.springboot.utils.JSON.stringify;
import static java.util.Objects.hash;
import static java.util.Optional.ofNullable;
import static java.util.stream.Stream.of;
import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Table(name = "user")
@Where(clause = NON_DELETED_CLAUSE)
public class User extends Auditable implements Serializable, Addressable {
    @Serial
    private static final long serialVersionUID = -8080540494839892473L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "deleted_email")
    private String deletedEmail;

    @JsonProperty(access = WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(cascade = DETACH, fetch = LAZY)
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setId(BigInteger id) {
        this.id = ofNullable(id)
                .map(BigInteger::longValue)
                .orElse(null);
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

    public void merge(UpdateUserProps props) {
        this.name = props.getName();
        this.email = props.getEmail();
    }

    public void updatePassword(String newPassword) {
        this.password = ENCODER.encode(newPassword);
    }

    public Boolean validatePassword(String password) {
        return ENCODER.matches(password, this.password);
    }

    @PrePersist
    private void created() {
        this.password = ENCODER.encode(password);
    }

    @Override
    public String toString() {
        return stringify(Map.of(
            "name", ofNullable(this.name).orElse(""),
            "email", ofNullable(this.email).orElse("")
        ));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return hash(getId());
    }

    public static User from(Tuple tuple) {
        var user = new User();
        user.setId(tuple.get("id", BigInteger.class));
        user.setName(tuple.get("name", String.class));
        user.setActive(tuple.get("active", Boolean.class));
        user.setEmail(tuple.get("email", String.class));
        user.setPassword(tuple.get("password", String.class));
        user.setRoles(ofNullable(tuple.get("roles", String.class))
                .map(roles -> of(roles.split(","))
                        .map(Role::new).toList())
                .orElse(List.of()));
        return user;
    }
}