package com.github.throyer.common.springboot.api.domain.services.user;

import static com.github.throyer.common.springboot.api.domain.models.pagination.Page.of;
import static com.github.throyer.common.springboot.api.domain.services.security.SecurityService.authorized;
import static com.github.throyer.common.springboot.api.utils.Responses.notFound;
import static com.github.throyer.common.springboot.api.utils.Responses.ok;
import static com.github.throyer.common.springboot.api.utils.Responses.unauthorized;
import static com.github.throyer.common.springboot.api.utils.SQLUtils.replace;
import static javax.persistence.criteria.JoinType.LEFT;

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
import com.github.throyer.common.springboot.api.domain.services.user.dto.UserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FindUserService {

    @Autowired
    UserRepository repository;

    public ResponseEntity<Page<UserDetails>> find(Pagination pagination, Sort sort, SearchUser search) {
        var result = repository.findAll(where(search), pagination.build(sort, User.class));
        return ok(of(result.map(UserDetails::new)));
    }

    public ResponseEntity<UserDetails> find(Long id) {
        return authorized()
                .filter(authorized -> authorized.cantRead(id)).map((authorized) -> repository
                        .findOptionalByIdAndDeletedAtIsNullFetchRoles(id).map(user -> ok(new UserDetails(user))).orElseGet(() -> notFound()))
                .orElse(unauthorized());
    }

    public static Specification<User> where(SearchUser search) {
        return (user, query, builder) -> {
            user.fetch("roles", LEFT);
            
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
                var role = user.join("roles", LEFT);

                predicates.add(builder.or(search
                    .getRoles()
                        .stream()
                            .map(name -> builder.or(
                                like(builder, role.get("initials"), name),
                                like(builder, role.get("name"), name),
                                like(builder, role.get("description"), name)
                            ))
                                .toList()
                                    .toArray(new Predicate[]{})));
            }

            query.select(query.from(User.class).join("roles"));
            
            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    private static Predicate like(CriteriaBuilder builder, Expression<String> expression, String value) {
        var lower = Normalizer.normalize(value.toLowerCase().trim(), Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return builder.like(replace(builder, expression), "%" + lower + "%");
    }
}
