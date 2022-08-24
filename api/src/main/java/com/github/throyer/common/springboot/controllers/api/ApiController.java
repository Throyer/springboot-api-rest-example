package com.github.throyer.common.springboot.controllers.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.throyer.common.springboot.utils.Hello;
import com.github.throyer.common.springboot.utils.Random;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api")
public class ApiController {

  @GetMapping
  @Operation(hidden = true)
  public Hello index() {

    var quotes = List.of(
        "You talking to me? - Taxi Driver",
        "I'm going to make him an offer he can't refuse. - The Godfather",
        "May the Force be with you. - Star Wars",
        "You're gonna need a bigger boat. - Jaws",
        "Dadinho é o caralho! meu nome é Zé Pequeno, porra! - Cidade de Deus",
        "Say “hello” to my little friend! - Scarface",
        "Bond. James Bond. - Dr. No",
        "Hasta la vista, baby. - Terminator 2",
        "I see dead people. - The Sixth Sense",
        "Houston, we have a problem. - Apollo 13",
        "Só sei que foi assim. - O Auto da Compadecida");

    var quote = Random.element(quotes);

    return () -> quote;
  }
}
