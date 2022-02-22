package com.github.throyer.common.springboot.domain.user.service;

import com.github.throyer.common.springboot.domain.pagination.model.Page;
import com.github.throyer.common.springboot.domain.user.model.UserDetails;
import com.github.throyer.common.springboot.domain.pagination.service.Pagination;

import com.github.throyer.common.springboot.domain.user.repository.custom.NativeQueryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindUserService {

    @Autowired
    NativeQueryUserRepository repository;

//    @Autowired
//    EntityManager manager;

    public Page<UserDetails> findAll(
        Optional<Integer> page,
        Optional<Integer> size
    ) {
//        var query = manager
//            .createNativeQuery(FIND_ALL_USER_DETAILS_FETCH_ROLES, Tuple.class);
//
//        var count = ((BigInteger) manager
//            .createNativeQuery(COUNT_ENABLED_USERS)
//                .getSingleResult())
//                    .longValue();
//
//        var pageable = Pagination.of(page, size);
//
//        var pageNumber = pageable.getPageNumber();
//        var pageSize = pageable.getPageSize();
//
//        query.setFirstResult(pageNumber * pageSize);
//        query.setMaxResults(pageSize);
//
//        List<Tuple> content = query.getResultList();
//
//        var users = content.stream().map(tuple -> new UserDetails(
//            tuple.get("id", BigInteger.class).longValue(),
//            tuple.get("name", String.class),
//            tuple.get("email", String.class),
//            tuple.get("roles", String.class)
//        )).toList();
        var pageable = Pagination.of(page, size);
        var result = repository.findAll(pageable);
//        return Page.of(users, pageNumber, pageSize, count);
        return null;
    }
}
