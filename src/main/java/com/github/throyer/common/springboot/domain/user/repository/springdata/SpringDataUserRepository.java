package com.github.throyer.common.springboot.domain.user.repository.springdata;

import com.github.throyer.common.springboot.domain.management.repository.SoftDeleteRepository;
import com.github.throyer.common.springboot.domain.user.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.github.throyer.common.springboot.domain.user.repository.Queries.DELETE_USER_BY_ID;

@Repository
public interface SpringDataUserRepository extends SoftDeleteRepository<User> {

    @Override
    @Transactional
    @Modifying
    @Query(DELETE_USER_BY_ID)
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

    Boolean existsByEmail(String email);

    @Query("""
        select user from #{#entityName} user
        left join fetch user.roles
        where user.id = ?1 
    """)
    Optional<User> findByIdFetchRoles(Long id);
}