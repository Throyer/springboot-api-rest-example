package com.github.throyer.common.springboot.api.configurations;

import com.github.throyer.common.springboot.api.services.SecurityService;

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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String QUALQUER_HTML = "/**.html";
    private static final String SWAGGER_DOCS = "/v2/api-docs";
    private static final String WEB_JARS = "/webjars/**";
    private static final String CONFIGURATIONS = "/configuration/**";
    private static final String SWAGGER_RESOURCES = "/swagger-resources/**";

    @Autowired
    private SecurityService autenticacaoService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private TokenFilter filter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(autenticacaoService)
            .passwordEncoder(encoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
                .disable()
                
            // rota publica (LOGIN).
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth/token")
                    .permitAll()
            
            // api de usuarios
                .antMatchers("/usuarios/**")
                    .hasAuthority("ADMINISTRADOR")
            
            // todas as outras rotas precisam de autenticação.
            .anyRequest()
                .authenticated()
            
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
                    QUALQUER_HTML,
                    SWAGGER_DOCS,
                    WEB_JARS,
                    CONFIGURATIONS,
                    SWAGGER_RESOURCES
            );
    }

}