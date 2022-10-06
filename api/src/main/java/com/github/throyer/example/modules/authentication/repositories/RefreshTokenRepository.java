package com.github.throyer.example.modules.authentication.repositories;

import static com.github.throyer.example.modules.authentication.repositories.Queries.DISABLE_OLD_REFRESH_TOKENS_FROM_USER;
import static com.github.throyer.example.modules.authentication.repositories.Queries.FIND_REFRESH_TOKEN_BY_CODE_FETCH_USER_AND_ROLES;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.throyer.example.modules.authentication.entities.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  @Transactional
  @Modifying
  @Query(DISABLE_OLD_REFRESH_TOKENS_FROM_USER)
  public void disableOldRefreshTokens(Long id);

  @Query(FIND_REFRESH_TOKEN_BY_CODE_FETCH_USER_AND_ROLES)
  public Optional<RefreshToken> findOptionalByCodeAndAvailableIsTrue(String code);
}
