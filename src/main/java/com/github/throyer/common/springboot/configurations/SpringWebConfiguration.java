package com.github.throyer.common.springboot.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(info = @Info(
    title = "Common CRUD API",
    version = "v4.0.1",
    description = """
        A complete user registry, with access permissions,
        JWT token, integration and unit tests, using
        the RESTful API pattern.
    """,
    license = @License(
        name = "GNU General Public License v3.0",
        url = "https://github.com/Throyer/springboot-api-crud/blob/master/LICENSE"
    ),
    contact = @Contact(
        name = "Throyer",
        email = "throyer.dev@gmail.com",
        url = "https://github.com/Throyer"
    )
))
@SecurityScheme(
    name = "token",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER,
    scheme = "bearer"
)
public class SpringWebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*");
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}