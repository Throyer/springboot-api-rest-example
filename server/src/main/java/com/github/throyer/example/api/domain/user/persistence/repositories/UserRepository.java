package com.github.throyer.example.api.domain.user.persistence.repositories;

import com.github.throyer.example.api.domain.user.persistence.models.User;
import com.github.throyer.example.api.shared.pagination.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
  Page<User> findAllFetchRoles(Pageable pageable);
  Optional<User> findOptionalByEmail(String email);
  Boolean existsByEmail(String email);
  Optional<User> findByIdFetchRoles(Long id);  
  void save(User user);
}
