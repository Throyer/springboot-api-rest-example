package com.github.throyer.common.springboot.domain.management.repository;

import com.github.throyer.common.springboot.domain.management.entity.Auditable;
import static com.github.throyer.common.springboot.domain.management.repository.Queries.DELETE_ALL;
import static com.github.throyer.common.springboot.domain.management.repository.Queries.DELETE_BY_ID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
public interface SoftDeleteRepository <T extends Auditable> extends JpaRepository<T, Long> {
    
    @Override
    @Modifying
    @Transactional
    @Query(DELETE_BY_ID)
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
    @Query(DELETE_ALL)
    void deleteAll();
}