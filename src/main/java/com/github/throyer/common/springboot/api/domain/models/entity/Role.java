package com.github.throyer.common.springboot.api.domain.models.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;

@Entity(name = "role")
@Where(clause = BasicEntity.NON_DELETED_CLAUSE)
public class Role extends BasicEntity implements GrantedAuthority {

    public static final String DELETE_SQL = """
        UPDATE 
            #{#entityName}
        SET
            deleted_name = (
                SELECT name FROM #{#entityName} WHERE id = ?1
            ),
            name = NULL,
            deleted_initials = (
                SELECT name FROM #{#entityName} WHERE id = ?1
            ),
            initials = NULL,
            deleted_at = CURRENT_TIMESTAMP,
            active = 0,
            deleted_by = ?#{principal?.id}
        WHERE id = ?1
    """;

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

    public Role() { }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeletedName() {
        return deletedName;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getDeletedInitials() {
        return deletedInitials;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean compare(String search) {
        if (Objects.nonNull(search)) {
            return 
                getName().toLowerCase().equals(search.toLowerCase()) ||
                getInitials().toLowerCase().equals(search.toLowerCase());
        }
        return false;
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