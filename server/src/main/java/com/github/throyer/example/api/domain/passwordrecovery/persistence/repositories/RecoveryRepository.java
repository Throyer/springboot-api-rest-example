package com.github.throyer.example.api.domain.passwordrecovery.persistence.repositories;

import com.github.throyer.example.api.domain.passwordrecovery.persistence.models.Recovery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecoveryRepository extends JpaRepository<Recovery, Long> {
  Optional<Recovery> findByCodeAndUser_Id(String code, Long id);
  Optional<Recovery> findFirstOptionalByUser_IdAndConfirmedIsFalseAndUsedIsFalseOrderByExpiresAtDesc(Long id);
  Optional<Recovery> findFirstOptionalByUser_IdAndConfirmedIsTrueAndUsedIsFalseOrderByExpiresAtDesc(Long id);
}
