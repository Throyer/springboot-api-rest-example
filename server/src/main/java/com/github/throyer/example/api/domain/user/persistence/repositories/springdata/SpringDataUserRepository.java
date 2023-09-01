package com.github.throyer.example.api.domain.user.persistence.repositories.springdata;

import com.github.throyer.example.api.domain.user.persistence.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
public interface SpringDataUserRepository extends JpaRepository<User, Long> {
  @EntityGraph(attributePaths = "roles", type = FETCH)
  Optional<User> findOptionalByEmail(String email);

  @EntityGraph(attributePaths = "roles", type = FETCH)
  Optional<User> findOptionalById(Long id);

  Boolean existsByEmail(String email);
}
