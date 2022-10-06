package com.github.throyer.example.modules.users.repositories.custom;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.github.throyer.example.modules.pagination.Page;
import com.github.throyer.example.modules.users.entities.User;

import java.util.Optional;

@Repository
public interface NativeQueryUserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Page<User> findAll(Pageable pageable);
}
