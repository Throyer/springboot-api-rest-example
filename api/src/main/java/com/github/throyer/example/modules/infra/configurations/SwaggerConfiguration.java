package com.github.throyer.example.modules.infra.configurations;

import static io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER;
import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfiguration {
  @Bean
  public OpenAPI springShopOpenAPI() {
    return new OpenAPI()
      .info(new Info()
        .title("Spring boot API example")
        .description("""
            A complete user registry, with access permissions,
            JWT token, integration and unit tests, using
            the RESTful API pattern.
          """)
        .version("v5.0.0")
        .license(new License()
          .name("GNU General Public License v3.0")
          .url("https://github.com/Throyer/springboot-api-rest-example/blob/master/LICENSE"))
        .contact(new Contact()
          .email("throyer.dev@gmail.com")
          .name("Renato Henrique Horacio dos Santos")
          .url("https://github.com/Throyer")))
      .components(new Components()
        .addSecuritySchemes("jwt", new SecurityScheme()
          .bearerFormat("JWT")
          .scheme("bearer")
          .type(HTTP)
          .in(HEADER)));
  }
}
