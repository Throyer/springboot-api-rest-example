package com.github.throyer.example;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.throyer.example.modules.shared.utils.Random;

import io.swagger.v3.oas.annotations.Operation;

@Controller
@RequestMapping("/")
public class HomeController {
  interface Hello {
    public String getMessage();
  }

  @GetMapping
  public String index() {
    return "redirect:/app";
  }

  @GetMapping("/app")
  public String app() {        
    return "app/index";
  }

  @ResponseBody
  @GetMapping("/api")
  @Operation(hidden = true)
  public Hello api() {
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
      "Só sei que foi assim. - O Auto da Compadecida"
    );

    var quote = Random.element(quotes);

    return () -> quote;
  }
}
