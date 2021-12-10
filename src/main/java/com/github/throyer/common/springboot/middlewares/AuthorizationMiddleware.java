package com.github.throyer.common.springboot.middlewares;

import static com.github.throyer.common.springboot.utils.TokenUtils.authorization;
import static java.util.Optional.ofNullable;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.throyer.common.springboot.domain.services.security.SecurityService;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthorizationMiddleware extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filter
    )
        throws ServletException, IOException {

        ofNullable(authorization(request))
            .ifPresent(SecurityService::authorize);

        filter.doFilter(request, response);
    }
}