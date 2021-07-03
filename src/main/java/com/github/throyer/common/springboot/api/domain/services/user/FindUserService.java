package com.github.throyer.common.springboot.api.domain.services.user;

import static com.github.throyer.common.springboot.api.domain.services.security.SecurityService.authorized;
import static com.github.throyer.common.springboot.api.utils.Responses.notFound;
import static com.github.throyer.common.springboot.api.utils.Responses.ok;
import static com.github.throyer.common.springboot.api.utils.Responses.unauthorized;
import static com.github.throyer.common.springboot.api.utils.SQLUtils.replace;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import com.github.throyer.common.springboot.api.domain.models.entity.User;
import com.github.throyer.common.springboot.api.domain.models.pagination.Page;
import com.github.throyer.common.springboot.api.domain.models.pagination.Pagination;
import com.github.throyer.common.springboot.api.domain.repositories.UserRepository;
import com.github.throyer.common.springboot.api.domain.services.user.dto.SearchUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FindUserService {

    @Autowired
    UserRepository repository;

    public ResponseEntity<Page<User>> find(Pagination pagination, Sort sort, SearchUser search) {
        return ok(Page.of(repository.findAll(where(search), pagination.build(sort, User.class))));
    }

    public ResponseEntity<User> find(Long id) {
        return authorized()
                .filter(authorized -> authorized.cantRead(id)).map((authorized) -> repository
                        .findOptionalByIdAndDeletedAtIsNull(id).map(user -> ok(user)).orElseGet(() -> notFound()))
                .orElse(unauthorized());
    }

    public static Specification<User> where(SearchUser search) {
        return (user, query, builder) -> {

            List<Predicate> predicates = new ArrayList<>();

            query.distinct(true);

            search
                .getName()
                    .ifPresent(name -> 
                        predicates.add(like(builder, user.get("name"), name)));

            search
                .getEmail()
                    .ifPresent(email ->
                        predicates.add(builder.equal(user.get("email"), email)));

            if (!search.getRoles().isEmpty()) {

                var role = user.join("roles");

                predicates.add(builder.or(search
                    .getRoles()
                        .stream()
                            .map(name -> builder.or(
                                like(builder, role.get("initials"), name),
                                like(builder, role.get("name"), name),
                                like(builder, role.get("description"), name)
                            ))
                                .toList()
                                    .toArray(new Predicate[search.getRoles().size()])));
            }
            
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    private static Predicate like(CriteriaBuilder builder, Expression<String> expression, String value) {
        var lower = Normalizer.normalize(value.toLowerCase().trim(), Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return builder.like(replace(builder, expression), "%" + lower + "%");
    }
}
