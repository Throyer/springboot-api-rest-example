package com.github.throyer.common.springboot.api.configurations;

import java.util.List;

import com.google.common.base.Predicates;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {

    private static final String AUTHORIZATION_PARAMETER_NAME = "Authorization";
    private static final String AUTHORIZATION_PARAMETER_DESCRIPRION = "Header para o token JWT";
    private static final String AUTHORIZATION_PARAMETER_CONTENT_TYPE = "string";
    private static final String AUTHORIZATION_PARAMETER_TYPE = "header";
    private static final Boolean AUTHORIZATION_PARAMETER_REQUIRED = false;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                    .apis(RequestHandlerSelectors.any())
                        .paths(PathSelectors.any())
                            .build()
                                .globalOperationParameters(getParameters());
    }

    private List<Parameter> getParameters() {
        return List.of(
            new ParameterBuilder()
                .name(AUTHORIZATION_PARAMETER_NAME)
                .description(AUTHORIZATION_PARAMETER_DESCRIPRION)
                .modelRef(new ModelRef(AUTHORIZATION_PARAMETER_CONTENT_TYPE))
                .parameterType(AUTHORIZATION_PARAMETER_TYPE)
                .required(AUTHORIZATION_PARAMETER_REQUIRED)
            .build()
        );
    }

}