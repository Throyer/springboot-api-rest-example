package com.github.throyer.example.api.domain.user.persistence.repositories.custom;

import com.github.throyer.example.api.domain.user.persistence.models.User;
import com.github.throyer.example.api.shared.pagination.Page;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.github.throyer.example.api.domain.user.persistence.repositories.custom.Queries.COUNT_IDS_FROM_USERS;
import static com.github.throyer.example.api.domain.user.persistence.repositories.custom.Queries.SELECT_DISTINCT_USERS_FETCH_ROLES;
import static com.github.throyer.example.api.domain.user.persistence.repositories.custom.Queries.SELECT_IDS_FROM_USERS;

@Repository
@AllArgsConstructor
public class CustomUserRepository {
  private final EntityManager manager;

  public Page<User> findAllFetchRoles(Pageable pageable) {
    var range = manager
      .createQuery(SELECT_IDS_FROM_USERS, Long.class);

    var query = manager
      .createQuery(SELECT_DISTINCT_USERS_FETCH_ROLES, User.class);

    var count = manager
      .createQuery(COUNT_IDS_FROM_USERS, Long.class).getSingleResult();

    var pageNumber = pageable.getPageNumber();
    var pageSize = pageable.getPageSize();

    range.setFirstResult(pageNumber * pageSize);
    range.setMaxResults(pageSize);

    List<Long> ids = range.getResultList();

    query.setParameter("user_ids", ids);

    List<User> content = query.getResultList();

    return Page.of(content, pageNumber, pageSize, count);
  }
}
