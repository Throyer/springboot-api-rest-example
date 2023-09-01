package com.github.throyer.example.api.domain.user.persistence.repositories.implementation;

import com.github.throyer.example.api.domain.user.persistence.models.User;
import com.github.throyer.example.api.domain.user.persistence.repositories.UserRepository;
import com.github.throyer.example.api.domain.user.persistence.repositories.custom.CustomUserRepository;
import com.github.throyer.example.api.domain.user.persistence.repositories.springdata.SpringDataUserRepository;
import com.github.throyer.example.api.shared.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {
  private final CustomUserRepository customUserRepository;
  private final SpringDataUserRepository springDataUserRepository;

  @Override
  public Page<User> findAllFetchRoles(Pageable pageable) {
    return customUserRepository.findAllFetchRoles(pageable);
  }

  @Override
  public Optional<User> findOptionalByEmail(String email) {
    return springDataUserRepository.findOptionalByEmail(email);
  }

  @Override
  public Boolean existsByEmail(String email) {
    return springDataUserRepository.existsByEmail(email);
  }

  @Override
  public void save(User user) {
    springDataUserRepository.save(user);
  }
  
  @Override
  public Optional<User> findByIdFetchRoles(Long id) {
    return springDataUserRepository.findOptionalById(id);
  }
}
