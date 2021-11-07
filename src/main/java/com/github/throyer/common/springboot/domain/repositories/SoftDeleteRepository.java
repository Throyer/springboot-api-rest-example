package com.github.throyer.common.springboot.domain.repositories;

import com.github.throyer.common.springboot.domain.models.entity.Auditable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
public interface SoftDeleteRepository <T extends Auditable> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    
    @Override
    @Modifying
    @Transactional
    @Query("""
        UPDATE
            #{#entityName}
        SET
            deleted_at = CURRENT_TIMESTAMP
        WHERE id = ?1
    """)
    void deleteById(Long id);
  
    @Override
    @Transactional
    default void delete(T entity) {
        deleteById(entity.getId());
    }

    @Override
    @Transactional
    default void deleteAll(Iterable<? extends T> entities) {
        entities.forEach(entity -> deleteById(entity.getId()));
    }
  
    @Override
    @Modifying
    @Transactional
    @Query("""
        UPDATE
            #{#entityName}
        SET
            deleted_at = CURRENT_TIMESTAMP
    """)
    void deleteAll();
}