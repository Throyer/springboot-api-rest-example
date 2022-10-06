package com.github.throyer.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import static org.springframework.boot.SpringApplication.run;

@EnableCaching
@SpringBootApplication
public class Main {
  public static void main(String... args) {
    System.setProperty("org.jooq.no-logo", "true");
    run(Main.class, args);
  }
}
