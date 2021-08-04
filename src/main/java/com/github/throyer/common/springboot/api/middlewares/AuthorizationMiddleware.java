package com.github.throyer.common.springboot.api.middlewares;

import static com.github.throyer.common.springboot.api.domain.services.security.SecurityService.authorize;
import static com.github.throyer.common.springboot.api.utils.TokenUtils.authorization;
import static java.util.Optional.ofNullable;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            .ifPresent((token) -> authorize(token));

        filter.doFilter(request, response);
    }
}