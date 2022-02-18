package com.github.throyer.common.springboot.domain.user.service;

import com.github.throyer.common.springboot.domain.role.repository.RoleRepository;
import com.github.throyer.common.springboot.domain.user.repository.UserRepository;
import com.github.throyer.common.springboot.domain.user.form.CreateUserProps;
import com.github.throyer.common.springboot.domain.user.entity.User;
import com.github.throyer.common.springboot.domain.user.model.UserDetails;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public CreateUserService(
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserDetails create(CreateUserProps props) {

        var roles = roleRepository.findOptionalByInitials("USER")
                .map(List::of)
                    .orElseGet(List::of);

        var user = userRepository.save(new User(props, roles));

        return new UserDetails(user);
    }
}
