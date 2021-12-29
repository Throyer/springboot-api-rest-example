package com.github.throyer.common.springboot.controllers.app;

import com.github.throyer.common.springboot.domain.models.pagination.Page;
import com.github.throyer.common.springboot.domain.models.pagination.Pagination;
import com.github.throyer.common.springboot.domain.repositories.UserRepository;
import com.github.throyer.common.springboot.domain.services.user.FindUserService;
import com.github.throyer.common.springboot.domain.services.user.dto.SearchUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasAnyAuthority('ADM')")
@RequestMapping("/app/users")
public class UserController {
    
    @Autowired
    private UserRepository repository;
    
    @GetMapping
    public String idnex(Model model, Pagination pagination, Sort sort, SearchUser search) {
        
        var page = Page.of(repository.findSimplifiedUsers(pagination.build()));
        
        model.addAttribute("page", page);
        
        return "/app/users/index";
    }
}
