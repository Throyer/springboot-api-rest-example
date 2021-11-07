package com.github.throyer.common.springboot.configurations;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static springfox.documentation.builders.PathSelectors.any;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.OAS_30;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Component
@Configuration
public class SwaggerConfiguration {

    @Autowired
    BuildProperties build;

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext
            .builder()
                .securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return asList(new SecurityReference("Authorization", authorizationScopes));
    }

    @Bean
    public Docket api() {
        return new Docket(OAS_30)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            .securityContexts(asList(securityContext()))
            .securitySchemes(asList(apiKey())).select()
            .apis(basePackage("com.github.throyer.common.springboot.controllers.api"))
            .paths(any())
            .build()
            .useDefaultResponseMessages(false)
            .globalResponses(HttpMethod.GET, List.of(
                new ResponseBuilder().code("500").description("Internal Server Error").build(),
                new ResponseBuilder().code("403").description("Forbidden").build(),
                new ResponseBuilder().code("404").description("Not Found").build()
            )).globalResponses(HttpMethod.POST, List.of(
                new ResponseBuilder().code("500").description("Internal Server Error").build(),
                new ResponseBuilder().code("403").description("Forbidden").build(),
                new ResponseBuilder().code("404").description("Not Found").build(),
                new ResponseBuilder().code("400").description("Bad Request").build(),
                new ResponseBuilder().code("422").description("Unprocessable Entity").build(),
                new ResponseBuilder().code("409").description("Conflict").build()
            )).globalResponses(HttpMethod.PUT, List.of(
                new ResponseBuilder().code("500").description("Internal Server Error").build(),
                new ResponseBuilder().code("403").description("Forbidden").build(),
                new ResponseBuilder().code("400").description("Bad Request").build(),
                new ResponseBuilder().code("422").description("Unprocessable Entity").build(),
                new ResponseBuilder().code("409").description("Conflict").build(),
                new ResponseBuilder().code("404").description("Not Found").build()
            )).globalResponses(HttpMethod.DELETE, List.of(
                new ResponseBuilder().code("500").description("Internal Server Error").build(),
                new ResponseBuilder().code("404").description("Not Found").build()
            ));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
            build.getName(),
            "Exemplo de api simples com Spring Boot",
            build.getVersion(),
            "https://github.com/Throyer",
            new Contact("Throyer", "https://github.com/Throyer", "throyer.dev@gmail.com"),
            "GNU General Public License v3.0",
            "https://github.com/Throyer/springboot-api-crud/blob/master/LICENSE",
            emptyList()
        );
    }
}