package com.github.throyer.example.modules.users.service;

import static com.github.throyer.example.modules.mail.validations.EmailValidations.validateEmailUniqueness;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.throyer.example.modules.roles.repositories.RoleRepository;
import com.github.throyer.example.modules.users.dtos.CreateUserProps;
import com.github.throyer.example.modules.users.entities.User;
import com.github.throyer.example.modules.users.repositories.UserRepository;

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

  public User create(CreateUserProps props) {
    validateEmailUniqueness(props);

    var roles = roleRepository.findOptionalByShortName("USER")
      .map(List::of)
        .orElseGet(List::of);

    var name = props.getName();
    var email = props.getEmail();
    var password = props.getPassword();

    return userRepository.save(new User(name, email, password, roles));
  }
}
