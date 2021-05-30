package com.github.throyer.common.springboot.api.repositories;

import java.util.Optional;

import com.github.throyer.common.springboot.api.models.entity.Role;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RoleRepository extends SoftDeleteRepository<Role> {

    @Override
    @Query(Role.DELETE_SQL)
    @Transactional
    @Modifying
    void deleteById(Long id);

    @Override
    @Transactional
    default void delete(Role role) {
        deleteById(role.getId());
    }

    @Override
    @Transactional
    default void deleteAll(Iterable<? extends Role> entities) {
        entities.forEach(entity -> deleteById(entity.getId()));
    }

    Optional<Role> findOptionalByInitials(String initials);

    Optional<Role> findOptionalByName(String name);

    Boolean existsByInitials(String initials);

    Boolean existsByName(String name);
}