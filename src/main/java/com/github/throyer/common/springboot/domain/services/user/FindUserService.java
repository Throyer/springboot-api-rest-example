package com.github.throyer.common.springboot.domain.services.user;

import static com.github.throyer.common.springboot.domain.services.security.SecurityService.authorized;
import static com.github.throyer.common.springboot.utils.Responses.notFound;
import static com.github.throyer.common.springboot.utils.Responses.ok;
import static com.github.throyer.common.springboot.utils.Responses.unauthorized;

import com.github.throyer.common.springboot.domain.models.pagination.Page;
import com.github.throyer.common.springboot.domain.repositories.UserRepository;
import com.github.throyer.common.springboot.domain.services.user.dto.UserDetails;
import com.github.throyer.common.springboot.domain.models.pagination.Pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        var sql = """
            select
            	u.id,
            	u.name,
            	u.email,
            	(select
            		GROUP_CONCAT(r.initials)
            	from role r
            		left join user_role ur on r.id = ur.role_id
            	where ur.user_id = u.id) as roles
            from 
                user u
            where u.deleted_at is null
        """;
        
        var query = manager.createNativeQuery(sql, Tuple.class);
        var count = ((BigInteger) manager.createNativeQuery("""
            select
            	count(u.id)
            from 
            	user u
            where u.deleted_at is null
        """).getSingleResult()).longValue();
        
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

    public ResponseEntity<UserDetails> find(Long id) {
        return authorized()
                .filter(authorized -> authorized.cantRead(id)).map((authorized) -> repository
                        .findOptionalByIdAndDeletedAtIsNullFetchRoles(id).map(user -> ok(new UserDetails(user))).orElseGet(() -> notFound()))
                .orElse(unauthorized());
    }
}
