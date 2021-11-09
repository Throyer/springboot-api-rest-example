package com.github.throyer.common.springboot.configurations;

import static com.github.throyer.common.springboot.errors.ValidationHandlers.forbidden;
import static com.github.throyer.common.springboot.utils.Constants.SECURITY.ACESSO_NEGADO_URL;
import static com.github.throyer.common.springboot.utils.Constants.SECURITY.DAY_MILLISECONDS;
import static com.github.throyer.common.springboot.utils.Constants.SECURITY.HOME_URL;
import static com.github.throyer.common.springboot.utils.Constants.SECURITY.LOGIN_ERROR_URL;
import static com.github.throyer.common.springboot.utils.Constants.SECURITY.LOGIN_URL;
import static com.github.throyer.common.springboot.utils.Constants.SECURITY.LOGOUT_URL;
import static com.github.throyer.common.springboot.utils.Constants.SECURITY.PASSWORD_PARAMETER;
import static com.github.throyer.common.springboot.utils.Constants.SECURITY.SESSION_COOKIE_NAME;
import static com.github.throyer.common.springboot.utils.Constants.SECURITY.STATIC_FILES;
import static com.github.throyer.common.springboot.utils.Constants.SECURITY.USERNAME_PARAMETER;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.github.throyer.common.springboot.domain.services.security.SecurityService;
import com.github.throyer.common.springboot.middlewares.AuthorizationMiddleware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfiguration {
    
    @Autowired
    private SecurityService securityService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private AuthorizationMiddleware filter;

    private static String SECRET;

    public SpringSecurityConfiguration(@Value("${token.secret}") String secret) {
        SpringSecurityConfiguration.SECRET = secret;
    }

    @Order(1)
    @Configuration
    public class Api extends WebSecurityConfigurerAdapter {
        
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(securityService)
                .passwordEncoder(encoder);
        }
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .antMatcher("/api/**")
                    .authorizeRequests()
                        .antMatchers(GET, "/api")
                            .permitAll()
                        .antMatchers(POST, "/api/users")
                            .permitAll()
                        .antMatchers(POST, "/api/sessions/**")
                            .permitAll()
                        .antMatchers(POST, "/api/recoveries/**")
                            .permitAll()
                        .antMatchers(GET, "/api/documentation/**")
                            .permitAll()
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
                    .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        }
    
        @Override
        public void configure(WebSecurity web) throws Exception {
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
                userDetailsService(securityService)
                    .passwordEncoder(encoder);
        }
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
    
            http
                .antMatcher("/app/**")
                    .authorizeRequests()
                        .antMatchers(GET, LOGIN_URL)
                            .permitAll()
                        .antMatchers(GET, "/app")
                            .permitAll()
                        .antMatchers(GET, "/app/register")
                            .permitAll()
                        .antMatchers(POST, "/app/register")
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
                        .key(SECRET)
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