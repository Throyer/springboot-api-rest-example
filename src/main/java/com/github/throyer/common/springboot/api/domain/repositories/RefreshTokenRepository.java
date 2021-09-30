package com.github.throyer.common.springboot.api.domain.repositories;

import java.util.Optional;

import javax.transaction.Transactional;

import com.github.throyer.common.springboot.api.domain.models.entity.RefreshToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>, JpaSpecificationExecutor<RefreshToken> {
    @Transactional
    @Modifying
    @Query("""
        UPDATE
            RefreshToken
        SET
            available = 0
        WHERE
            user_id = ?1 AND available = 1
    """)
    public void disableOldRefreshTokens(Long id);

    @Query("""
        SELECT refresh FROM RefreshToken refresh
        JOIN FETCH refresh.user user
        JOIN FETCH user.roles
        WHERE refresh.code = ?1 AND refresh.available = true
    """)
    public Optional<RefreshToken> findOptionalByCodeAndAvailableIsTrue(String code);
}
