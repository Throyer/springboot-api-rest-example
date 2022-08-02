package com.github.throyer.common.springboot.domain.role.entity;

import static com.github.throyer.common.springboot.domain.management.repository.Queries.NON_DELETED_CLAUSE;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.throyer.common.springboot.domain.management.entity.Auditable;

import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;

@Getter
@Entity
@Table(name = "role")
@Where(clause = NON_DELETED_CLAUSE)
public class Role extends Auditable implements GrantedAuthority {

    private static final long serialVersionUID = -8524505911742593369L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = true, unique = true)
    private String name;

    @JsonIgnore
    @Column(name = "deleted_name")
    private String deletedName;

    @Column(name = "initials", nullable = true, unique = true)
    private String initials;

    @JsonIgnore
    @Column(name = "deleted_initials")
    private String deletedInitials;

    @Column(nullable = true, unique = true)
    private String description;

    public Role() {
    }

    public Role(String initials) {
        this.initials = initials;
    }

    public Role(Long id) {
        this.id = id;
    }

    public Role(Long id, String initials) {
        this.id = id;
        this.initials = initials;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (!(object instanceof Role)) {
            return false;
        }
        Role role = (Role) object;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return this.getAuthority();
    }

    @JsonIgnore
    @Override
    public String getAuthority() {
        return this.getInitials();
    }

}