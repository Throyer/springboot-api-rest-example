package com.github.throyer.common.springboot.configurations;

import com.github.throyer.common.springboot.domain.session.service.SessionService;
import com.github.throyer.common.springboot.middlewares.AuthorizationMiddleware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import static com.github.throyer.common.springboot.utils.Constants.SECURITY.*;
import static com.github.throyer.common.springboot.utils.Responses.forbidden;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Component
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfiguration {

    private final SessionService sessionService;
    private final AuthorizationMiddleware filter;

    @Autowired
    public SpringSecurityConfiguration(
            SessionService sessionService,
            AuthorizationMiddleware filter
    ) {
        this.sessionService = sessionService;
        this.filter = filter;
    }

    @Order(1)
    @Configuration
    public class Api extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(sessionService)
                .passwordEncoder(PASSWORD_ENCODER);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            PUBLIC_API_ROUTES.configure(http);

            http
                .antMatcher("/api/**")
                    .authorizeRequests()
                        .anyRequest()
                            .authenticated()
                .and()
                    .csrf()
                    .disable()
                        .exceptionHandling()
                            .authenticationEntryPoint((request, response, exception) -> forbidden(response))
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(STATELESS)
                .and()
                    .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .cors()
                    .configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
        }

        @Override
        public void configure(WebSecurity web) {
            web
                .ignoring()
                    .antMatchers(STATIC_FILES);
        }

        @Bean
        @Override
        protected AuthenticationManager authenticationManager() throws Exception {
            return super.authenticationManager();
        }
    }

    @Order(2)
    @Configuration
    public class App extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.
                userDetailsService(sessionService)
                    .passwordEncoder(PASSWORD_ENCODER);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                .antMatcher("/app/**")
                    .authorizeRequests()
                        .antMatchers(GET, LOGIN_URL, "/app", "/app/register", "/app/recovery/**")
                            .permitAll()
                        .antMatchers(POST, "/app/register", "/app/recovery/**")
                            .permitAll()
                        .anyRequest()
                            .authenticated()
                .and()
                    .csrf()
                        .disable()
                            .formLogin()
                                .loginPage(LOGIN_URL)
                                    .failureUrl(LOGIN_ERROR_URL)
                                    .defaultSuccessUrl(HOME_URL)
                                .usernameParameter(USERNAME_PARAMETER)
                                .passwordParameter(PASSWORD_PARAMETER)
                .and()
                    .rememberMe()
                        .key(TOKEN_SECRET)
                            .tokenValiditySeconds(DAY_MILLISECONDS)
                .and()
                    .logout()
                        .deleteCookies(SESSION_COOKIE_NAME)
                            .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_URL))
                            .logoutSuccessUrl(LOGIN_URL)
                .and()
                    .exceptionHandling()
                        .accessDeniedPage(ACESSO_NEGADO_URL);
        }
    }
}