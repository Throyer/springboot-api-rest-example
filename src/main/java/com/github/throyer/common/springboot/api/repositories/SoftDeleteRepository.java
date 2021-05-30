package com.github.throyer.common.springboot.api.repositories;

import com.github.throyer.common.springboot.api.models.shared.BasicEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
public interface SoftDeleteRepository <T extends BasicEntity> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    
    @Override
    @Query(BasicEntity.SET_DELETED_SQL)
    @Transactional
    @Modifying
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
    @Query(BasicEntity.SET_ALL_DELETED_SQL)
    @Transactional
    @Modifying
    void deleteAll();
}