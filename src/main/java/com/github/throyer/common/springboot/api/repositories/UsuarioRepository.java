package com.github.throyer.common.springboot.api.repositories;

import java.util.Optional;

import com.github.throyer.common.springboot.api.models.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    public Boolean existsByEmail(String email);

    public Optional<Usuario> findOptionalByEmail(String email);
}