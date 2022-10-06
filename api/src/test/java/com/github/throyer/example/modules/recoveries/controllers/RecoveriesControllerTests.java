package com.github.throyer.example.modules.recoveries.controllers;

import static com.github.throyer.example.modules.shared.utils.Random.user;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

import com.github.throyer.example.modules.recoveries.repositories.RecoveryRepository;
import com.github.throyer.example.modules.shared.utils.JSON;
import com.github.throyer.example.modules.users.repositories.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureDataJpa
@AutoConfigureMockMvc
class RecoveriesControllerTests {

  @Autowired
  private RecoveryRepository recoveryRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MockMvc api;

  @Test
  @DisplayName("Deve responder No Content quando email for invalido.")
  void should_return_no_content_on_invalid_email() throws Exception {

    var json = JSON.stringify(Map.of(
        "email", "jubileu.da.silva@email.fake.com"));

    api.perform(
        post("/api/recoveries")
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .content(json))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("Deve responder No Content quando email for valido.")
  void should_return_no_content_on_valid_email() throws Exception {

    var user = user();

    userRepository.save(user);

    var json = JSON.stringify(Map.of(
        "email", user.getEmail()));

    api.perform(
        post("/api/recoveries")
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .content(json))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("Deve confirmar o código quando ele for valido for valido.")
  void should_confirm_code_when_is_valid() throws Exception {

    var user = userRepository.save(user());

    var recoveryBody = JSON.stringify(Map.of(
        "email", user.getEmail()));

    api.perform(
        post("/api/recoveries")
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .content(recoveryBody))
        .andExpect(status().isNoContent());

    var unconfirmedRecovery = recoveryRepository
        .findFirstOptionalByUser_IdAndConfirmedIsFalseAndUsedIsFalseOrderByExpiresAtDesc(user.getId())
        .orElseThrow(() -> new RuntimeException("recovery code not found"));

    assertFalse(unconfirmedRecovery.isConfirmed());
    assertFalse(unconfirmedRecovery.isUsed());

    var confirmBody = JSON.stringify(Map.of(
        "email", user.getEmail(),
        "code", unconfirmedRecovery.getCode()));

    api.perform(
        post("/api/recoveries/confirm")
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .content(confirmBody))
        .andExpect(status().isNoContent());

    var confirmedRecovery = recoveryRepository
        .findFirstOptionalByUser_IdAndConfirmedIsTrueAndUsedIsFalseOrderByExpiresAtDesc(user.getId())
        .orElseThrow(() -> new RuntimeException("recovery code not found"));

    assertTrue(confirmedRecovery.isConfirmed());
    assertFalse(unconfirmedRecovery.isUsed());
  }
}
