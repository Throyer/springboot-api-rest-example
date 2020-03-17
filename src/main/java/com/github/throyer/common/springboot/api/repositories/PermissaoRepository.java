package com.github.throyer.common.springboot.api.repositories;

import java.util.Optional;

import com.github.throyer.common.springboot.api.models.entity.Permissao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

    Optional<Permissao> findOptionalByNome(String nome);

    Boolean existsByNome(String nome);
}