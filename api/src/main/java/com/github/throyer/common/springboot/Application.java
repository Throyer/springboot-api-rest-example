package com.github.throyer.common.springboot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import static org.springframework.boot.SpringApplication.run;

@EnableCaching
@SpringBootApplication
public class Application {
  public static void main(String... args) {
    run(Application.class, args);
  }
}
