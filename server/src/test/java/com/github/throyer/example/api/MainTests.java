package com.github.throyer.example.api;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static java.lang.System.setProperty;

@TestPropertySource(properties = {
  "spring.datasource.driver-class-name=org.h2.Driver",
  "spring.datasource.url=jdbc:h2:mem:test;mode=PostgreSQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1",
  "spring.datasource.username=sa",
  "spring.datasource.password=sa",
})
@Tag("Unit")
@SpringBootTest
class MainTests {
	@Test
	void contextLoads() { }
}
