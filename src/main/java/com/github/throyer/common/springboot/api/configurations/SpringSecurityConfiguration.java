package com.github.throyer.common.springboot.api.configurations;

import com.github.throyer.common.springboot.api.services.security.SecurityService;

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
    private static final String SWAGGER_DOCS = "/v2/api-docs";
    private static final String WEBJARS = "/webjars/**";
    private static final String CONFIGURATIONS = "/configuration/**";
    private static final String SWAGGER_RESOURCES = "/swagger-resources/**";

    @Autowired
    private SecurityService authService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private TokenFilter filter;

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
                
            // rota publica (LOGIN).
                .antMatchers(HttpMethod.POST, "/auth/token")
                    .permitAll()

            // rota publica de cadastro de usuários.
                .antMatchers(HttpMethod.POST, "/users")
                    .permitAll()
                        
            // todas as outras rotas precisam de autenticação.
            .anyRequest()
                .fullyAuthenticated()
            
            // configuração de acesso.
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
                .antMatchers(
                    EVERY_HTML,
                    SWAGGER_DOCS,
                    WEBJARS,
                    CONFIGURATIONS,
                    SWAGGER_RESOURCES
            );
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}