package com.github.throyer.common.springboot.api.configurations;

import static com.github.throyer.common.springboot.api.errors.ValidationHandlers.forbidden;

import com.github.throyer.common.springboot.api.domain.services.security.SecurityService;
import com.github.throyer.common.springboot.api.middlewares.AuthorizationMiddleware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String EVERY_HTML = "/**.html";
    private static final String SWAGGER_DOCS = "/documentation/**";

    @Autowired
    private SecurityService authService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private AuthorizationMiddleware filter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authService)
            .passwordEncoder(encoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
                .disable()
                    .authorizeRequests()
                        .antMatchers(HttpMethod.GET, "/api/")
                            .permitAll()
                        .antMatchers(HttpMethod.POST, "/api/users")
                            .permitAll()
                        .antMatchers(HttpMethod.POST, "/api/sessions/**")
                            .permitAll()
                        .antMatchers(HttpMethod.POST, "/api/recoveries/**")
                            .permitAll()
                        .antMatchers(HttpMethod.GET, "/api/documentation/**")
                            .permitAll()
                        .antMatchers("/api/**")
                            .authenticated()
            .and()
                .exceptionHandling()
                    .authenticationEntryPoint((request, response, exception) -> forbidden(response))
            .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
                .antMatchers(EVERY_HTML, SWAGGER_DOCS);
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}