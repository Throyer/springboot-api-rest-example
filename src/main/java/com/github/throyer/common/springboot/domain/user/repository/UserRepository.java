package com.github.throyer.common.springboot.domain.user.repository;

import java.util.Optional;

import com.github.throyer.common.springboot.domain.user.entity.User;
import com.github.throyer.common.springboot.domain.user.model.UserDetails;
import static com.github.throyer.common.springboot.domain.user.repository.Queries.DELETE_USER_BY_ID;
import static com.github.throyer.common.springboot.domain.user.repository.Queries.FIND_ALL_USER_DETAILS_WITHOUT_ROLES;
import static com.github.throyer.common.springboot.domain.user.repository.Queries.FIND_USERNAME_BY_ID;
import static com.github.throyer.common.springboot.domain.user.repository.Queries.FIND_USER_BY_ID_FETCH_ROLES;
import com.github.throyer.common.springboot.domain.management.repository.SoftDeleteRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static com.github.throyer.common.springboot.domain.user.repository.Queries.FIND_USER_BY_EMAIL_FETCH_ROLES;

@Repository
public interface UserRepository extends SoftDeleteRepository<User> {

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

    public Page<User> findDistinctBy(Pageable pageable);

    public Boolean existsByEmail(String email);
    
    public Optional<User> findOptionalByIdAndDeletedAtIsNull(Long id);
    
    @Query(FIND_USERNAME_BY_ID)
    public Optional<String> findNameById(Long id);

    @Query(FIND_USER_BY_ID_FETCH_ROLES)
    public Optional<User> findOptionalByIdFetchRoles(Long id);

    @Query(FIND_USER_BY_EMAIL_FETCH_ROLES)
    public Optional<User> findOptionalByEmailFetchRoles(String email);

    public Optional<User> findOptionalByEmail(String email);
    
    @Query(FIND_ALL_USER_DETAILS_WITHOUT_ROLES)
    public Page<UserDetails> finAllUserDetailsWithoutRoles(Pageable pageable);
}