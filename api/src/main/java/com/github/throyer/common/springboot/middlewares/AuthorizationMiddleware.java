package com.github.throyer.common.springboot.middlewares;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.throyer.common.springboot.domain.session.service.SessionService.authorize;

@Component
@Order(1)
public class AuthorizationMiddleware extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filter
    ) throws ServletException, IOException {
        authorize(request, response);
        filter.doFilter(request, response);
    }
}