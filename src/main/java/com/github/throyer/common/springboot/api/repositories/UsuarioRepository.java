package com.github.throyer.common.springboot.api.repositories;

import java.util.Optional;

import com.github.throyer.common.springboot.api.models.entity.Usuario;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UsuarioRepository extends SoftDeleteRepository<Usuario> {

    @Override
    @Query(Usuario.DELETE_SQL)
    @Transactional
    @Modifying
    void deleteById(Long id);

    @Override
    @Transactional
    default void delete(Usuario usuario) {
        deleteById(usuario.getId());
    }

    @Override
    @Transactional
    default void deleteAll(Iterable<? extends Usuario> entities) {
        entities.forEach(entitiy -> deleteById(entitiy.getId()));
    }

    public Boolean existsByEmail(String email);

    public Optional<Usuario> findOptionalByEmail(String email);
}