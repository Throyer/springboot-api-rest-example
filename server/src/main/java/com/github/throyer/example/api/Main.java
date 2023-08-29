package com.github.throyer.example.api;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.System.setProperty;
import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class Main {
	public static void main(String... args) {
    setProperty("org.jooq.no-logo", "true");
		run(Main.class, args);
	}
}
