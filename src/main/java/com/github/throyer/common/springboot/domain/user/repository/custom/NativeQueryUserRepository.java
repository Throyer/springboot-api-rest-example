package com.github.throyer.common.springboot.domain.user.repository.custom;

import com.github.throyer.common.springboot.domain.pagination.model.Page;
import com.github.throyer.common.springboot.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NativeQueryUserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Page<User> findAll(Pageable pageable);
}
