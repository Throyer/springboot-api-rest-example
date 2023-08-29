package com.github.throyer.example.api.domain.role.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.throyer.example.api.domain.role.persistence.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  List<Role> findByShortNameIn(List<String> names);
  
  Optional<Role> findOptionalByShortName(String shortName);

  Optional<Role> findOptionalByName(String name);

  Boolean existsByShortName(String shortName);

  Boolean existsByName(String name);
}
