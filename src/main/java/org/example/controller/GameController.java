package org.example.controller;


import org.example.model.Game;
import org.example.model.User;
import org.example.repository.GameRepository;
import org.example.response.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("games")
public class GameController {

    @Autowired
    private GameRepository gameRepository;

    @GetMapping
    public ResponseEntity<CustomResponse> getAllGames() {
        if (gameRepository.count() < 1) {
            CustomResponse errResponse = new CustomResponse("Error", new Error("No data found"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errResponse);
        }

        return ResponseEntity.ok(new CustomResponse("success", this.gameRepository.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse> getGameByID(@PathVariable int id) {
        if (!gameRepository.existsById(id)) {
            CustomResponse errResponse = new CustomResponse("Error", new Error("Id is not in database!"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errResponse);
        }

        Game game = this.gameRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found!"));
        return ResponseEntity.ok(new CustomResponse("Success", game));
    }

    @PostMapping
    public ResponseEntity<CustomResponse> createGame(@RequestBody Game game) {
        if (game.getGameDate() == null || game.getWordle() == null || game.getGuesses() == 0 || game.getUser() == null) {
            CustomResponse errResponse = new CustomResponse("Error", new Error("Check if all fields are correct!"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errResponse);
        }
        return ResponseEntity.ok(new CustomResponse("success", this.gameRepository.save(game)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse> deleteGameByID(@PathVariable int id) {

        if (!gameRepository.existsById(id)) {
            CustomResponse errResponse = new CustomResponse("Error", new Error("Id is not in database!"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errResponse);
        }

        Game game = this.gameRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found!"));
        this.gameRepository.delete(game);
        game.setUser(new User());
        return ResponseEntity.ok(new CustomResponse("Success", game));


    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse> updateGame(@PathVariable int id, @RequestBody Game game) {


        Game previousGame = this.gameRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        Game gameToUpdate = this.gameRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found!"));
        if (game.getScore() == 0) {
            gameToUpdate.setScore(previousGame.getScore());
        }
        else {
            gameToUpdate.setScore(game.getScore());
        }
        if (game.getWordle() == null) {
            gameToUpdate.setWordle(previousGame.getWordle());
        }
        else {
            gameToUpdate.setWordle(game.getWordle());
        }
        if (game.getGuesses() == 0) {
            gameToUpdate.setGuesses(previousGame.getGuesses());
        }
        else {
            gameToUpdate.setGuesses(game.getGuesses());
        }
        if (game.getGameDate() == null) {
            gameToUpdate.setGameDate(previousGame.getGameDate());
        }
        else {
            gameToUpdate.setGameDate(game.getGameDate());
        }
        if (game.getUser() == null) {
            gameToUpdate.setUser(previousGame.getUser());
        }
        else {
            gameToUpdate.setUser(game.getUser());
        }



        return ResponseEntity.ok(new CustomResponse("Success", this.gameRepository.save(gameToUpdate)));
    }




}
