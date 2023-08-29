package com.github.throyer.example.api.domain.user.services;

import com.github.throyer.example.api.domain.user.persistence.models.User;
import com.github.throyer.example.api.domain.user.persistence.repositories.UserRepository;
import com.github.throyer.example.api.shared.pagination.Page;
import com.github.throyer.example.api.shared.pagination.Pagination;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@Slf4j
@Service
@AllArgsConstructor
public class FindAllUsersService {
  private final UserRepository repository;

  public Page<User> find(Integer page, Integer size) {
    var pageable = Pagination.of(ofNullable(page), ofNullable(size));

    log.info(format("find users. [page: %s, size: %s]", pageable.getPageNumber(), pageable.getPageSize()));    
    
    var result = repository.findAllFetchRoles(pageable);

    log.info("user search performed successfully.");
    
    return result;
  }
}
