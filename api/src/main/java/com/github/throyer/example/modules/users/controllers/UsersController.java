package com.github.throyer.example.modules.users.controllers;

import static com.github.throyer.example.modules.infra.http.Responses.created;
import static com.github.throyer.example.modules.infra.http.Responses.ok;
import static com.github.throyer.example.modules.shared.utils.HashIdsUtils.decode;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.throyer.example.modules.pagination.Page;
import com.github.throyer.example.modules.users.dtos.CreateUserProps;
import com.github.throyer.example.modules.users.dtos.UpdateUserProps;
import com.github.throyer.example.modules.users.dtos.UserInformation;
import com.github.throyer.example.modules.users.service.CreateUserService;
import com.github.throyer.example.modules.users.service.FindUserService;
import com.github.throyer.example.modules.users.service.RemoveUserService;
import com.github.throyer.example.modules.users.service.UpdateUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Users")
@RequestMapping("/api/users")
public class UsersController {        
  private final CreateUserService createService;
  private final UpdateUserService updateService;
  private final RemoveUserService removeService;
  private final FindUserService findService;

  @Autowired
  public UsersController(
    CreateUserService createService,
    UpdateUserService updateService,
    RemoveUserService removeService,
    FindUserService findService
  ) {
    this.createService = createService;
    this.updateService = updateService;
    this.removeService = removeService;
    this.findService = findService;
  }

  @GetMapping
  @SecurityRequirement(name = "token")
  @PreAuthorize("hasAnyAuthority('ADM')")
  @Operation(summary = "Returns a list of users")
  public ResponseEntity<Page<UserInformation>> index(
    Optional<Integer> page,
    Optional<Integer> size
  ) {
    var response = findService.find(page, size);
    return ok(response.map(UserInformation::new));
  }
  
  @GetMapping("/{user_id}")
  @SecurityRequirement(name = "token")
  @PreAuthorize("hasAnyAuthority('ADM', 'USER')")
  @Operation(summary = "Show user info")
  public ResponseEntity<UserInformation> show(@PathVariable("user_id") String id) {
    var user = findService.find(decode(id));
    return ok(new UserInformation(user));
  }
  
  @PostMapping
  @ResponseStatus(CREATED)
  @Operation(summary = "Register a new user", description = "Returns the new user")
  public ResponseEntity<UserInformation> save(
    @Validated @RequestBody CreateUserProps props
  ) {
    var user = new UserInformation(createService.create(props));
    return created(user, "api/users", user.getId());
  }
  
  @PutMapping("/{user_id}")
  @SecurityRequirement(name = "token")
  @PreAuthorize("hasAnyAuthority('ADM', 'USER')")
  @Operation(summary = "Update user data")
  public ResponseEntity<UserInformation> update(
    @PathVariable("user_id") String id,
    @RequestBody @Validated UpdateUserProps body
  ) {
    var user = updateService.update(decode(id), body);
    return ok(new UserInformation(user));
  }
  
  @DeleteMapping("/{user_id}")
  @ResponseStatus(NO_CONTENT)
  @SecurityRequirement(name = "token")
  @Operation(summary = "Delete user")
  public void destroy(@PathVariable("user_id") String id) {
    removeService.remove(decode(id));
  }
}