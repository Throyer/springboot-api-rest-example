package com.github.throyer.common.springboot.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class CommonApiApplication {

	public static void main(String... args) {
		System.getProperties().setProperty("org.jooq.no-logo", "true");
		SpringApplication.run(CommonApiApplication.class, args);
	}
}
