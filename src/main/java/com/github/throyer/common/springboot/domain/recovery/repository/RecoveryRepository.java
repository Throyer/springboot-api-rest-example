package com.github.throyer.common.springboot.domain.recovery.repository;

import java.util.Optional;

import com.github.throyer.common.springboot.domain.recovery.entity.Recovery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RecoveryRepository extends JpaRepository<Recovery, Long>, JpaSpecificationExecutor<Recovery> {
    public Optional<Recovery> findFirstOptionalByUser_IdAndConfirmedIsFalseAndUsedIsFalseOrderByExpiresInDesc(Long id);
    public Optional<Recovery> findFirstOptionalByUser_IdAndConfirmedIsTrueAndUsedIsFalseOrderByExpiresInDesc(Long id);
}
