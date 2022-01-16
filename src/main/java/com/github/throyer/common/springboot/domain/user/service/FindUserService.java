package com.github.throyer.common.springboot.domain.user.service;

import com.github.throyer.common.springboot.domain.pagination.model.Page;
import com.github.throyer.common.springboot.domain.user.repository.UserRepository;
import com.github.throyer.common.springboot.domain.user.model.UserDetails;
import static com.github.throyer.common.springboot.domain.user.repository.Queries.COUNT_ENABLED_USERS;
import static com.github.throyer.common.springboot.domain.user.repository.Queries.FIND_ALL_USER_DETAILS_FETCH_ROLES;
import com.github.throyer.common.springboot.domain.pagination.service.Pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;

@Service
public class FindUserService {

    @Autowired
    UserRepository repository;

    @Autowired
    EntityManager manager;

    public Page<UserDetails> findAll(
        Optional<Integer> page,
        Optional<Integer> size
    ) {
        var query = manager
            .createNativeQuery(FIND_ALL_USER_DETAILS_FETCH_ROLES, Tuple.class);
        
        var count = ((BigInteger) manager
            .createNativeQuery(COUNT_ENABLED_USERS)
                .getSingleResult())
                    .longValue();
        
        var pageable = Pagination.of(page, size);
        
        var pageNumber = pageable.getPageNumber();
        var pageSize = pageable.getPageSize();
        
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);
        
        List<Tuple> content = query.getResultList();
        
        var users = content.stream().map(tuple -> new UserDetails(
            tuple.get("id", BigInteger.class).longValue(),
            tuple.get("name", String.class),
            tuple.get("email", String.class),
            tuple.get("roles", String.class)
        )).toList();
        
        return Page.of(users, pageNumber, pageSize, count);
    }
}
