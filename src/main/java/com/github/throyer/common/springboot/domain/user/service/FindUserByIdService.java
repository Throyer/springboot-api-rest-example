package com.github.throyer.common.springboot.domain.user.service;

import static com.github.throyer.common.springboot.domain.session.service.SessionService.authorized;
import static com.github.throyer.common.springboot.utils.Responses.notFound;
import static com.github.throyer.common.springboot.utils.Responses.unauthorized;

import com.github.throyer.common.springboot.domain.user.repository.UserRepository;
import com.github.throyer.common.springboot.domain.user.model.UserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindUserByIdService {
    
    @Autowired
    UserRepository repository;
    
    public UserDetails find(Long id) {
        authorized()
            .filter(authorized -> authorized.itsMeOrSessionIsADM(id))
                .orElseThrow(() -> unauthorized("Invalid permission to read resource"));
        
        var user = repository
            .findOptionalByIdFetchRoles(id)
                .orElseThrow(() -> notFound("User not found")); 
        
        return new UserDetails(user);
    }
}
