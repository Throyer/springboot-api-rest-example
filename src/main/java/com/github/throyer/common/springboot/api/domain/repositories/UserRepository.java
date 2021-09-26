package com.github.throyer.common.springboot.api.domain.repositories;

import java.util.Optional;

import com.github.throyer.common.springboot.api.domain.models.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends SoftDeleteRepository<User> {

    @Override
    @Query(User.DELETE_SQL)
    @Transactional
    @Modifying
    void deleteById(Long id);

    @Override
    @Transactional
    default void delete(User user) {
        deleteById(user.getId());
    }

    @Override
    @Transactional
    default void deleteAll(Iterable<? extends User> entities) {
        entities.forEach(entity -> deleteById(entity.getId()));
    }

    public Page<User> findDistinctBy(Pageable pageable);

    public Boolean existsByEmail(String email);
    
    public Optional<User> findOptionalByIdAndDeletedAtIsNull(Long id);

    @Query("""
        SELECT user FROM User user
        JOIN FETCH user.roles
        WHERE user.id = ?1
    """)
    public Optional<User> findOptionalByIdAndDeletedAtIsNullFetchRoles(Long id);

    public Optional<User> findOptionalByEmail(String email);
}