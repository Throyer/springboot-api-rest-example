package com.github.throyer.example.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/")
public class Home {
  
  @Getter
  @AllArgsConstructor
  static class Movie {
    private String name;
    private String quote;
  }

  @GetMapping
  @Operation(hidden = true)
  public Movie home() {
    var quotes = List.of(
      new Movie("Taxi Driver", "You talking to me?"),
      new Movie("The Godfather", "I'm going to make him an offer he can't refuse."),
      new Movie("Star Wars", "May the Force be with you."),
      new Movie("Jaws", "You're gonna need a bigger boat"),
      new Movie("Cidade de Deus", "Dadinho é o caralho! meu nome é Zé Pequeno, porra!"),
      new Movie("Scarface", "Say “hello” to my little friend!"),
      new Movie("Dr. No", "Bond. James Bond."),
      new Movie("Terminator 2","Hasta la vista, baby."),
      new Movie("The Sixth Sense", "I see dead people."),
      new Movie("Apollo 13", "Houston, we have a problem."),
      new Movie("O Auto da Compadecida", "Só sei que foi assim.")
    );
    
    return quotes.get(new Random().nextInt(quotes.size()));
  }
}
