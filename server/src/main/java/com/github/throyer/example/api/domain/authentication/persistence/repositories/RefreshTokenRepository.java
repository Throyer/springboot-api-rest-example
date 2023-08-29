package com.github.throyer.example.api.domain.authentication.persistence.repositories;

import com.github.throyer.example.api.domain.authentication.persistence.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.github.throyer.example.api.domain.authentication.persistence.repositories.Queries.DISABLE_OLD_REFRESH_TOKENS_FROM_USER;
import static com.github.throyer.example.api.domain.authentication.persistence.repositories.Queries.FIND_REFRESH_TOKEN_BY_CODE_FETCH_USER_AND_ROLES;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  @Query(DISABLE_OLD_REFRESH_TOKENS_FROM_USER)
  void disableOldRefreshTokensFromUser(@Param("user_id") Long userId);

  @Query(FIND_REFRESH_TOKEN_BY_CODE_FETCH_USER_AND_ROLES)
  Optional<RefreshToken> findOptionalByCodeAndAvailableIsTrue(@Param("code") String code);
}
