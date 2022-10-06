package com.github.throyer.example.modules.roles.repositories;

import static com.github.throyer.example.modules.roles.repositories.Queries.DELETE_ROLE_BY_ID;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.github.throyer.example.modules.management.repositories.SoftDeleteRepository;
import com.github.throyer.example.modules.roles.entities.Role;

@Repository
public interface RoleRepository extends SoftDeleteRepository<Role> {

  @Override
  @Modifying
  @Transactional
  @Query(DELETE_ROLE_BY_ID)
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

  Optional<Role> findOptionalByShortName(String shortName);

  Optional<Role> findOptionalByName(String name);

  Boolean existsByShortName(String shortName);

  Boolean existsByName(String name);
}