package com.github.throyer.common.springboot.domain.session.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import com.github.throyer.common.springboot.domain.session.entity.RefreshToken;
import static com.github.throyer.common.springboot.domain.session.repository.Queries.DISABLE_OLD_REFRESH_TOKENS_FROM_USER;
import static com.github.throyer.common.springboot.domain.session.repository.Queries.FIND_REFRESH_TOKEN_BY_CODE_FETCH_USER_AND_ROLES;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>, JpaSpecificationExecutor<RefreshToken> {
    @Transactional
    @Modifying
    @Query(DISABLE_OLD_REFRESH_TOKENS_FROM_USER)
    public void disableOldRefreshTokens(Long id);

    @Query(FIND_REFRESH_TOKEN_BY_CODE_FETCH_USER_AND_ROLES)
    public Optional<RefreshToken> findOptionalByCodeAndAvailableIsTrue(String code);
}
