package com.github.throyer.common.springboot.configurations;

import static com.github.throyer.common.springboot.constants.SECURITY.ACESSO_NEGADO_URL;
import static com.github.throyer.common.springboot.constants.SECURITY.DAY_MILLISECONDS;
import static com.github.throyer.common.springboot.constants.SECURITY.HOME_URL;
import static com.github.throyer.common.springboot.constants.SECURITY.LOGIN_ERROR_URL;
import static com.github.throyer.common.springboot.constants.SECURITY.LOGIN_URL;
import static com.github.throyer.common.springboot.constants.SECURITY.LOGOUT_URL;
import static com.github.throyer.common.springboot.constants.SECURITY.PASSWORD_ENCODER;
import static com.github.throyer.common.springboot.constants.SECURITY.PASSWORD_PARAMETER;
import static com.github.throyer.common.springboot.constants.SECURITY.PRIVATE_SWAGGER;
import static com.github.throyer.common.springboot.constants.SECURITY.PUBLIC_API_ROUTES;
import static com.github.throyer.common.springboot.constants.SECURITY.SESSION_COOKIE_NAME;
import static com.github.throyer.common.springboot.constants.SECURITY.TOKEN_SECRET;
import static com.github.throyer.common.springboot.constants.SECURITY.USERNAME_PARAMETER;
import static com.github.throyer.common.springboot.utils.Responses.forbidden;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.github.throyer.common.springboot.domain.session.service.SessionService;
import com.github.throyer.common.springboot.middlewares.AuthorizationMiddleware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

@Component
@Configuration
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

    @Autowired
    protected void globalConfiguration(
        AuthenticationManagerBuilder authentication
    ) throws Exception {
        authentication
            .userDetailsService(sessionService)
            .passwordEncoder(PASSWORD_ENCODER);
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }
    
    @Bean
    @Order(1)
    public SecurityFilterChain api(HttpSecurity http) throws Exception {
        PUBLIC_API_ROUTES.injectOn(http);

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

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain app(HttpSecurity http) throws Exception {
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

        return http.build();
    }

    @Bean
    @Order(4)
    public SecurityFilterChain swagger(HttpSecurity http) throws Exception {

        if (PRIVATE_SWAGGER) {
            http
                .authorizeRequests()
                        .antMatchers("/swagger-ui/**", "/swagger-ui.html", "/**.html", "/documentation/**")
                            .authenticated()
                .and()
                    .httpBasic();
        } else {
            http
                .authorizeRequests()
                        .antMatchers("/swagger-ui/**", "/swagger-ui.html", "/**.html", "/documentation/**")
                            .permitAll();
        }

        return http.build();
    }
}