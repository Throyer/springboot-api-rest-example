package com.github.throyer.common.springboot.domain.repositories;

import java.util.Optional;

import com.github.throyer.common.springboot.domain.models.entity.User;
import com.github.throyer.common.springboot.domain.services.user.dto.UserDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends SoftDeleteRepository<User> {

    @Override
    @Transactional
    @Modifying
    @Query("""
        UPDATE
            #{#entityName}
        SET
            deleted_email = (
                SELECT
                    email
                FROM
                    #{#entityName}
                WHERE id = ?1),
            email = NULL,
            deleted_at = CURRENT_TIMESTAMP,
            active = false,
            deleted_by = ?#{principal?.id}
        WHERE id = ?1
    """)
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
        SELECT user.name FROM #{#entityName} user
        WHERE user.id = ?1
    """)
    public Optional<String> findNameById(Long id);

    @Query("""
        SELECT user FROM #{#entityName} user
        LEFT JOIN FETCH user.roles
        WHERE user.id = ?1
    """)
    public Optional<User> findOptionalByIdAndDeletedAtIsNullFetchRoles(Long id);

    @Query("""
        SELECT user FROM #{#entityName} user
        LEFT JOIN FETCH user.roles
        WHERE user.email = ?1
    """)
    public Optional<User> findOptionalByEmailFetchRoles(String email);

    public Optional<User> findOptionalByEmail(String email);
    
    @Query("""
        SELECT
            new com.github.throyer.common.springboot.domain.services.user.dto.UserDetails(
                user.id,
                user.name,
                user.email
            )
        FROM #{#entityName} user
    """)
    public Page<UserDetails> findSimplifiedUsers(Pageable pageable);
}