package com.github.throyer.common.springboot.api.services.user;

import static com.github.throyer.common.springboot.api.services.common.validation.EmailValidations.validateEmailUniqueness;
import static com.github.throyer.common.springboot.api.utils.Responses.created;

import java.util.List;

import com.github.throyer.common.springboot.api.domain.entity.User;
import com.github.throyer.common.springboot.api.repositories.RoleRepository;
import com.github.throyer.common.springboot.api.repositories.UserRepository;
import com.github.throyer.common.springboot.api.services.user.dto.CreateUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roles;

    public ResponseEntity<User> create(CreateUser create) {
        
        validateEmailUniqueness(create);

        var body = create.toUser();

        body.setRoles(
            List.of(
                roles.findOptionalByInitials("USER")
                    .orElseThrow()
            )
        );

        var user = userRepository.save(body);

        return created(user, "users");
    }
}
